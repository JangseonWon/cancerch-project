package com.gcgenome.lims.service.request

import com.gcgenome.lims.entity.QPatient
import com.gcgenome.lims.entity.QPatient.patient
import com.gcgenome.lims.entity.QRequest.request
import com.gcgenome.lims.entity.QSample
import com.gcgenome.lims.entity.QSample.sample
import com.gcgenome.lims.entity.QService
import com.gcgenome.lims.entity.QService.service
import com.gcgenome.lims.projection.Request
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import com.querydsl.core.types.Projections.constructor
import reactor.core.publisher.Mono

@Component
class RequestDao(
    private val requestRepo: RequestRepository
) {
    private val qPatient = QPatient("patient")
    private val qService = QService("service")
    private val qSample = QSample("sample")
    private fun select(query: SQLQuery<*>): SQLQuery<Request.Companion.RequestBuilder> {
        return query.select(
            constructor(
                Request.Companion.RequestBuilder::class.java,
                patient.customerName.`as`("institutionName"),
                patient.customerName2.`as`("institution2Name"),
                patient.customerDeptName.`as`("departmentName"),
                patient.ward.`as`("wardName"),
                service.serviceNm.`as`("serviceName"),
                patient.name.`as`("patientName"),
                patient.sex.`as`("sex"),
                patient.birth.`as`("birth"),
                patient.mrn.`as`("mrn"),
                request.info.`as`("info"),
                patient.customerCode.`as`("institution"),
                patient.customerCode2.`as`("institution2"),
                patient.physician.`as`("physician"),
                request.sample.`as`("sample"),
                sample.sampleType.`as`("sampleType"),
                request.dateRequest.`as`("dateRequest"),
                request.dateReception.`as`("dateReception"),
                request.dateSampling.`as`("dateSampling"),
                request.dateDue.`as`("dateDuePublish"),
                patient.age.`as`("age"),
                sample.remark.`as`("remark"),
                request.service.`as`("service")
            )
        ).from(request)
            .leftJoin(qSample).on(request.sample.eq(qSample.id))
            .leftJoin(qPatient).on(qPatient.id_SET.eq(qSample.patient))
            .leftJoin(qService).on(qService.serviceId.eq(request.service))
    }
    fun findById(sample: Long, service: String): Mono<Request> {
        return requestRepo.query {
            select(it).where(request.sample.eq(sample).and(request.service.eq(service)))
        }.one().map(Request.Companion.RequestBuilder::build)
    }
}
