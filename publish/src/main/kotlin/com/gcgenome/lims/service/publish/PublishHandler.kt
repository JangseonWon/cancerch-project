package com.gcgenome.lims.service.publish

import com.gcgenome.alis.Client
import com.gcgenome.alis.FileUpload
import com.gcgenome.alis.Request
import com.gcgenome.lims.service.report.ReportDao
import com.gcgenome.lims.service.reportfile.ReportFileRepository
import com.greencross.lims.jandiwebhook.Webhook
import com.greencross.lims.jandiwebhook.dto.ConnectInfo
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO

@Service
class PublishHandler(
    val reportDao: ReportDao,
    val reportFileRepo: ReportFileRepository,
    val client: Client,
    val jandi: Webhook
    ) {
    private val logger = LoggerFactory.getLogger(PublishHandler::class.java)
    @Transactional
    fun publish(sample: Long, service: String, createAt: Long) : Mono<Boolean> {
        val title = "검사 결과 전송에 실패했습니다. (8ㅁ8)"
        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            .flatMap {
                val sampleId = it.sample
                val requestNum = (it.sample % 10000000).toInt()
                val requestDate =
                    LocalDate.parse(String.format("%d", sampleId / 10000000), DateTimeFormatter.ofPattern("yyyyMMdd"))
                val requestAlis = Request(requestDate, requestNum, it.service)
                val data = reportFileRepo.findById(it.file).map { it2 ->
                    it2.data!!.array()
                }.get()

                getUser().flatMap{ user ->
                    client.state(requestAlis, "F", user.authentication.principal.toString(), "LIMS")
                }.map{result ->
                    if(!result)  Exception("Delete failure")
                    else result}.zipWith(getUser())
                .flatMap { tuple -> createImgDiv(tuple.t2.authentication.principal.toString(), data, requestAlis) }
                .map{result ->
                    if(!result)  Exception("Change failure")
                    else result}.zipWith(getUser())
                .flatMap { tuple -> sendToAlis(tuple.t2.authentication.principal.toString(), data, requestAlis, "pdf", "") }
                .map{result ->
                    if(!result)  Exception("PDF failure")
                    else result}.zipWith(getUser())
                .flatMap { tuple ->
                    client.state(requestAlis, "I", tuple.t2.authentication.principal.toString(), "LIMS")
                }.flatMap{ _ ->
                    reportDao.merge(it.sample, it.service, it.createAt)
                }
                .then(Mono.just(true))
                    .doOnError{
                        when(it.message){
                            "Delete failure" -> jandi.sendWithConnectInfos(title, listOf(ConnectInfo()
                                .title("실패 대상 : ${requestNum}/${service}").description("기존 파일 삭제 실패")))
                            "Change failure" -> jandi.sendWithConnectInfos(title, listOf(ConnectInfo()
                                .title("실패 대상 : ${requestNum}/${service}").description("ALIS 상태 변경 실패")))
                            "PDF failure"    -> jandi.sendWithConnectInfos(title, listOf(ConnectInfo()
                                .title("실패 대상 : ${requestNum}/${service}").description("PDF 전송 실패")))
                            else             -> jandi.sendWithConnectInfos(title, listOf(ConnectInfo()
                                .title("실패 대상 : ${requestNum}/${service}").description("원인 미상 LIMS팀 확인 필요")))
                        }
                    }
                .switchIfEmpty(Mono.just(false))
            }

    }
    private fun sendToAlis(user: String, data: ByteArray, request: Request, flg: String, page: String) : Mono<Boolean> {
        val prefix = request.requestDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val infix  = String.format("%d", request.requestNo/10000)
        val suffix = String.format("%d", request.requestNo%10000)
        val title = "${prefix}_${infix}${suffix}"

        logger.info("Sent To Alis")
        return when (flg) {
            "pdf" -> {
                logger.info("PDF")
                client.fileUpload(FileUpload(user, request, data, title, "PDF", LocalDate.now(), ""))
            }
            "imgDiv" -> {
                logger.info("imgDiv")
                client.fileUpload(FileUpload(user, request, data, title + "_" + page, "JPG_PER_PAGE", LocalDate.now(), ""))
            }
            "imgTotal" -> {
                logger.info("imgTotal")
                client.fileUpload(FileUpload(user, request, data, title, "JPG", LocalDate.now(), ""))
            }
            else -> {
                logger.info("DEFAULT")
                Mono.just(false)
            }
        }
    }
    private fun createImgDiv(user: String, data: ByteArray, request: Request) : Mono<Boolean> {
        val doc: PDDocument = PDDocument.load(data)
        val renderer = PDFRenderer(doc)
        return Flux.fromIterable((0 until doc.numberOfPages).toList())
            .map { it to renderer.renderImageWithDPI(it, 93f, ImageType.RGB) }
            .map{ it.first to bufferedImageToBytes(it.second)}
            .flatMap{ (page, bytes) -> sendToAlis(user, bytes, request, "imgDiv", page.toString())}
            .filter{!it}
            .elementAt(0)
            .onErrorResume(IndexOutOfBoundsException::class.java) {Mono.just(true)}
    }
    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }

    private fun bufferedImageToBytes(img: BufferedImage) : ByteArray{
        val baos = ByteArrayOutputStream()
        ImageIO.write(img, "jpg", baos)
        baos.flush()
        baos.close()
        return baos.toByteArray()
    }
}