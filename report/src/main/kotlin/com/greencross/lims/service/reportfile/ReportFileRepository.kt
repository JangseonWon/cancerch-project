package com.greencross.lims.service.reportfile

import com.greencross.lims.entity.ReportFile
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReportFileRepository : CassandraRepositoryExtended<ReportFile, UUID> {
}