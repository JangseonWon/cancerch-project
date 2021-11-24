package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.Sample
import com.greencross.lims.data.Request_
import com.greencross.lims.data.Patient
import com.greencross.lims.data.Work_
import com.greencross.lims.repo.*
import org.springframework.stereotype.Service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import kotlin.Comparator

@Service
class WorklistDetailHandler (
    private val om: ObjectMapper,
    private val ServiceRepo: ServiceRepository,
    private val SampleRepo: SampleRepository,
    private val RequestRepo: RequestRepository,
    private val PatientRepo: PatientRepository,
    private val WorkRepo: WorkRepository,
    private val mapper: WorklistDetailToDto
) {
    fun list(id: String): Flux<Work_>{
        return WorkRepo.findByWorklist(id).map(mapper::toDto).sort(Comparator.comparing { r->r.no })
    }
    fun sample(yesterday: String, today: String): Flux<Request_> {
        return RequestRepo.findByServiceAndDateRequestBetween("N201", yesterday, today)
            .flatMap(this::toDto).sort(Comparator.comparing { r->r.sample })
    }
    private fun toDto(entity: com.greencross.lims.entity.Request): Mono<Request_>{
        return SampleRepo.findById(entity.sample.toString())
            .flatMap(this::toDto)
            .zipWith(ServiceRepo.findById(entity.service))
            .map {p->Request_(
                sample = entity.sample,
                info = entity.info,
                service = entity.service,
                dateRequest = entity.dateRequest,
                dateEnd = entity.dateEnd,
                type = p.t1.type,
                remark = p.t1.remark,
                patient = p.t1.patient,
                barcode = p.t1.barcode,
                customerName = p.t1.customerName,
                customerCode = p.t1.customerCode,
                mrn = p.t1.mrn,
                name = p.t1.name,
                code = p.t1.code,
                sex = p.t1.sex,
                serviceName = p.t2.name
            )}
    }
    private fun toDto(entity: com.greencross.lims.entity.Sample): Mono<Sample>{
        return PatientRepo.findById(entity.patient)
            .map(this::toDto)
            .map { p->Sample(
                type = entity.sampleType,
                remark = entity.remark,
                patient = entity.patient,
                barcode = entity.barcode,
                customerName = p.customerName,
                customerCode = p.customerCode,
                mrn = p.mrn,
                name = p.name,
                code = p.code,
                sex = p.sex
            )}
    }
    private fun toDto(entity: com.greencross.lims.entity.Patient): Patient{
        return Patient(
            customerName = entity.customerName,
            customerCode = entity.customerCode,
            mrn = entity.mrn,
            name = entity.name,
            code = entity.code,
            sex = entity.sex
        )
    }
}