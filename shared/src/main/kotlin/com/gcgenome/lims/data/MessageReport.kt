package com.gcgenome.lims.data

data class MessageReport(
    val type: MessageType,
    val data: Report
){
    constructor() : this(MessageType._NULL, Report())
    enum class MessageType {
        _NULL, CREATE, PRINTING, FINISH
    }
}
