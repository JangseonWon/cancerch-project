package com.gcgenome.lims.service.lims1

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Batch (
    val template: UUID = UUIDEnum.TEMPLATE.toUUID(),
    val idx: Int = 0
) {
    var title: String = ""
    val value: MutableMap<UUID, String> = mutableMapOf()
    val analysis: MutableList<Analysis> = mutableListOf()
}