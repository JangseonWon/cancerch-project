package com.gcgenome.lims.service.publish

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.EventConfig
import com.gcgenome.alis.Client
import com.gcgenome.alis.models.AlisResponse
import com.gcgenome.lims.model.ReportForRMS
import com.gcgenome.lims.model.ResultInfo
import com.gcgenome.lims.projection.Report
import com.gcgenome.lims.service.report.ReportDao
import com.gcgenome.lims.service.reportfile.ReportFileRepository
import com.gcgenome.lims.service.request.RequestDao
import com.gcgenome.lims.workflow.*
import com.greencross.lims.jandiwebhook.Webhook
import com.greencross.lims.jandiwebhook.dto.ConnectInfo
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

@Service
class PublishHandler(
    private val reportDao: ReportDao,
    private val requestDao: RequestDao,
    private val reportFileRepo: ReportFileRepository,
    private val client: Client,
    private val om: ObjectMapper,
    private val jandi: Webhook,
    private val event: EventConfig
) {
    private val subscriber = Sinks.many().multicast().directAllOrNothing<AlisResponse>()
    private val rmsPublisher = Sinks.many().unicast().onBackpressureBuffer<ReportForRMS>()
    private val logger = LoggerFactory.getLogger(PublishHandler::class.java)

    fun subscribe(): Flux<AlisResponse> = subscriber.asFlux()
    fun publish(sample: Long, service: String, createAt: Long, request: ServerRequest): Mono<Boolean> {
        return reportDao.findForCassandraReport(
            sample,
            service,
            LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId())
        )
            .zipWith(getUser())
            .flatMap { publishAlis(sample, service, request, it) }
            .flatMap { publishRMS(sample, service, createAt) }
            .filter { it == true }
            .flatMap {
                reportDao.merge(
                    sample,
                    service,
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId())
                )
            }.map {
                logger.info("의뢰번호 : "+sample+" / 검사코드 : "+service+" 전송 완료")
                true
            }
            .switchIfEmpty(Mono.just(false))
    }

    private fun getUser(): Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }

    @Bean("broadcast-publishing")
    fun broadcastPublish(): Consumer<String> {
        return Consumer { c: String -> subscriber.tryEmitNext(stringToMessage(c)) }
    }

    @Bean("publish")
    fun publish(): Supplier<Flux<String>> {
        return Supplier {
            rmsPublisher.asFlux().mapNotNull {
                try {
                    om.writeValueAsString(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }


    private fun stringToMessage(str: String): AlisResponse {
        return om.readValue(str, AlisResponse::class.java)
    }

    private fun publishAlis(
        sample: Long,
        service: String,
        request: ServerRequest,
        it: Tuple2<Report, SecurityContext>
    ): Mono<Boolean> {
        val file = it.t1.file
        val resultInfo = it.t1.resultInfo
        val user = it.t2.authentication
        logger.info("ALIS 전송 시작")
        return requestDao.findById(sample, service)
            .flatMap { client.send(it, file, user, request, om.readValue(resultInfo, ResultInfo::class.java)) }
    }

    private fun publishRMS(sample: Long, service: String, createAt: Long): Mono<Boolean> {
        logger.info("의뢰번호 : "+sample+" / 검사코드 : "+service+" RMS 전송 시작")
        return requestDao.findById(sample, service)
            .zipWith(reportDao.findForCassandraReport(
                sample,
                service,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId())
            ).publishOn(Schedulers.boundedElastic()).map { reportFileRepo.findById(it.file).map { it.data?.array() }.get() }
            ).map {
                val institution = if(it.t1.institution2.isNullOrBlank())it.t1.institution else it.t1.institution + "-" + it.t1.institution2
                ReportForRMS(
                    it.t1.institutionName,
                    it.t1.departmentName,
                    it.t1.wardName,
                    it.t1.serviceName,
                    it.t1.patientName,
                    it.t1.sex,
                    it.t1.birth.toString(),
                    it.t1.mrn,
                    it.t1.info,
                    institution,
                    it.t1.physician,
                    it.t1.sample,
                    it.t1.service,
                    it.t2
                ) to Event(
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    Request(
                        it.t1.sample.toString()+":"+it.t1.service,
                        Organization(
                            it.t1.institution?:"미입력",
                            it.t1.institutionName?:"미입력"
                        ),
                        Service(
                            it.t1.service,
                            it.t1.serviceName
                        ),
                        listOf(
                            Sample(
                            it.t1.sample,
                            it.t1.sampleType?:"-",
                            Patient(
                                Organization(
                                    if(it.t1.institution2!=null) it.t1.institution2?:"-" else it.t1.institution?:"-",
                                    if(it.t1.institution2!=null) it.t1.institution2Name?:"-" else it.t1.institutionName?:"-"
                                ),
                                it.t1.patientName,
                                    if(it.t1.sex == "M") Patient.Companion.Sex.M else Patient.Companion.Sex.F
                                ,
                                Patient.Companion.Birth(
                                    (it.t1.birth?: LocalDate.of(1900, 1,1)).year,
                                    (it.t1.birth?: LocalDate.of(1900, 1,1)).monthValue,
                                    (it.t1.birth?: LocalDate.of(1900, 1,1)).dayOfMonth),
                                it.t1.mrn
                            ),
                            it.t1.dateSampling,
                            it.t1.age,
                            it.t1.remark
                            )
                        ),
                        it.t1.dateRequest,
                        it.t1.dateReception,
                        it.t1.dateDuePublish
                    ),
                    "avoid-publish",
                    EventProcess.FINISHED,
                    EventType.COMPLETE,
                    ParamImpl("New"),
                    "avoid-publisher"
                )
            }.flatMap { (report, eventObj) ->
                val rmsResult = rmsPublisher.tryEmitNext(report)
                if (rmsResult.isFailure) {
                    return@flatMap Mono.error<Boolean>(RuntimeException("RMS Kafka 메시지 발행 실패 ($sample / $service)"))
                }
                event.publishEvent(eventObj)
                logger.info("의뢰번호 : $sample / 검사코드 : $service RMS Kafka 메시지 발행 완료")
                Mono.just(true)
            }
            .onErrorResume {
                jandi.sendWithConnectInfos("RMS 결과지 전송 중 오류가 발생했습니다. 재전송이 필요합니다. ($sample / $service)",
                    listOf(ConnectInfo().title("")))
                Mono.just(false)
            }
    }

    companion object {
        class ParamImpl(override val processInfo: String) : HashMap<String, Any>(), Param
    }
}
