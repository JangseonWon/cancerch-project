package com.gcgenome.lims.service.lims1

import java.util.*

data class Batch (
    val template: UUID = UUIDEnum.TEMPLATE.toUUID(),
    val idx: Int = 0,
    val state: String = "CREATE"
) {
    var title: String = ""
    val value: MutableMap<UUID, String> = mutableMapOf()
    val analysis: MutableList<Analysis> = mutableListOf()
}