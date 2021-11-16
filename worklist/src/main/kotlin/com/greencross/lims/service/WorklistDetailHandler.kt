package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.Sample
import com.greencross.lims.data.Request_
import com.greencross.lims.data.Patient
import com.greencross.lims.repo.PatientRepository
import com.greencross.lims.repo.RequestRepository
import com.greencross.lims.repo.SampleRepository
import com.greencross.lims.repo.ServiceRepository
import org.springframework.stereotype.Service

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class WorklistDetailHandler (
    private val om: ObjectMapper,
    private val ServiceRepo: ServiceRepository,
    private val SampleRepo: SampleRepository,
    private val RequestRepo: RequestRepository,
    private val PatientRepo: PatientRepository,
    private val mapper: WorklistDetailToDto
) {
    fun sample(): Flux<Request_> {
        return RequestRepo.findByService("N201")
            .flatMap(this::toDto)
    }
    private fun toDto(entity: com.greencross.lims.entity.Request): Mono<Request_>{
        return SampleRepo.findById(entity.sample.toString())
            .flatMap(this::toDto)
            .map {p->Request_(
                sample = entity.sample,
                info = entity.info,
                service = entity.service,
                dateRequest = entity.dateRequest,
                type = p.type,
                remark = p.remark,
                patient = p.patient,
                mrn = p.mrn,
                name = p.name,
                code = p.code,
                sex = p.sex
            )}
//            .zipWith(ServiceRepo.findById(entity.service))
//            .map { p->Request_(
//                id = p.t1.id,
//                type = p.t1.type,
//                remark = p.t1.remark,
//                customerName = p.t1.customerName,
//                mrn = p.t1.mrn,
//                name = p.t1.name,
//                code = p.t1.code,
//                sex = p.t1.sex,
//                dateRequest = entity.dateRequest,
//                sample = p.sample,
//                info = entity.info,
//                serviceName = p.t2.name
//            )}
    }
    private fun toDto(entity: com.greencross.lims.entity.Sample): Mono<Sample>{
        return PatientRepo.findById(entity.patient)
            .map(this::toDto)
            .map { p->Sample(
                type = entity.sampleType,
                remark = entity.remark,
                patient = entity.patient,
                customerName = p.customerName,
                mrn = p.mrn,
                name = p.name,
                code = p.code,
                sex = p.sex.toString()
            )}
    }
    private fun toDto(entity: com.greencross.lims.entity.Patient): Patient{
        return Patient(
            customerName = entity.customerName,
            mrn = entity.mrn,
            name = entity.name,
            code = entity.code,
            sex = entity.sex
        )
    }
}