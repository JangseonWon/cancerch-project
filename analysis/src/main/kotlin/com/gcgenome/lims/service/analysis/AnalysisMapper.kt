package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.data.*
import com.gcgenome.lims.entity.Analysis
import org.springframework.stereotype.Component

@Component
class AnalysisMapper {
    fun toDto(entity: Analysis) : com.gcgenome.lims.data.Analysis{
        return com.gcgenome.lims.data.Analysis(entity.sample, entity.serviceId, entity.batch, entity.row).apply{
            this.freemix                        = entity.freemix
            this.rawReadsMillions               = entity.rawReadsMillions
            this.dupRate                        = entity.dupRate
            this.gc                             = entity.gc
            this.totalReads                     = entity.totalReads
            this.mean                           = entity.mean
            this.qc                             = entity.qc
            this.median                         = entity.median
            this.chrXCnt                        = entity.chrXCnt
            this.chrYCnt                        = entity.chrYCnt
            this.chrXProp                       = entity.chrXProp
            this.chrYProp                       = entity.chrYProp
            this.predSex                        = entity.predSex

            this.freemixTmp                     = entity.freemixTmp
            this.rawReadsMillionsTmp            = entity.rawReadsMillionsTmp
            this.dupRateTmp                     = entity.dupRateTmp
            this.gcTmp                          = entity.gcTmp
            this.totalReadsTmp                  = entity.totalReadsTmp
            this.meanTmp                        = entity.meanTmp
            this.qcTmp                          = entity.qcTmp
            this.medianTmp                      = entity.medianTmp
            this.chrXCntTmp                     = entity.chrXCntTmp
            this.chrYCntTmp                     = entity.chrYCntTmp
            this.chrXPropTmp                    = entity.chrXPropTmp
            this.chrYPropTmp                    = entity.chrYPropTmp
            this.predSexTmp                     = entity.predSexTmp

            this.too5Pred                       = entity.too5Pred
            this.too5FemsProb                   = entity.too5FemsProb
            this.too6Pred                       = entity.too6Pred
            this.too6FemsProb                   = entity.too6FemsProb
            this.iscore                         = entity.iscore
            this.cadEnsembleProb                = entity.cadEnsembleProb
            this.result                         = entity.result
            this.comment                        = entity.comment ?: ""

            this.request = Request(
                Sample(entity.sample).apply{
                    this.barcode = entity.barcode.toString()
                    this.patient = Patient(
                        entity.patientName,
                        entity.mrn,
                        entity.sex,
                        entity.birth.toString(),
                        when(entity.customerName2){
                            null -> entity.customerName
                            else -> entity.customerName2
                        }
                    )
                    this.remark = entity.remark
                }
            ).apply {
                this.service = Service(entity.serviceId, entity.serviceNm)
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
            this.createdAt = entity.analysisAt.toString()
            this.lastModifyAt = entity.lastModifyAt.toString()
            this.lastModifyBy = User(entity.lastModifyById, entity.lastModifyByNm)
        }
    }
}