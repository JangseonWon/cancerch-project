package com.gcgenome.lims.service.reportfile

import com.gcgenome.lims.entity.ReportFile
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReportFileRepository : CassandraRepositoryExtended<ReportFile, UUID> {
}