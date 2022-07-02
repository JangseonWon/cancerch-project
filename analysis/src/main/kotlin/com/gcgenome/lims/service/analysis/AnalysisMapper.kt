package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.data.*
import com.gcgenome.lims.entity.Analysis
import org.springframework.stereotype.Component

@Component
class AnalysisMapper {
    fun toDto(entity: Analysis) : com.gcgenome.lims.data.Analysis{
        return Analysis(entity.sample, entity.serviceId).apply{
            this.batch = entity.batch
            this.row = entity.row
            this.request = Request(
                Sample(entity.sample).apply{
                    this.barcode = entity.barcode.toString()
                    this.patient = Patient(
                        entity.patientName,
                        entity.mrn,
                        entity.sex,
                        entity.birth.toString(),
                        entity.customerName
                    )
                }
            ).apply {
                this.service = Service(entity.serviceId, entity.serviceNm)
                this.serial = entity.serial
                this.dateRequest = entity.dateRequest.toString()
                this.dateStart = entity.dateStart.toString()
                this.dateDue = entity.dateDue.toString()
                this.dateSampling = entity.dateSampling.toString()
                this.registered = entity.register.toString()
                this.canceled = entity.cancel.toString()
                this.deleted = entity.delete.toString()
            }
            this.report = Report(
                entity.sample, entity.serviceId, entity.reportedAt.toString(), entity.reportName, entity.size, entity.reportUrl,
                User(entity.reportedById, entity.reportedByNm), entity.publishAt.toString(), User(entity.publishById, entity.publishByNm)
            )
            this.file = entity.analysisFile
            this.value = entity.value
            this.createdAt = entity.analysisAt.toString()
            this.createdBy = User(entity.analysisById, entity.analysisByNm)
            this.lastModifyAt = entity.lastModifyAt.toString()
            this.lastModifyBy = User(entity.lastModifyById, entity.lastModifyByNm)
        }
    }
}