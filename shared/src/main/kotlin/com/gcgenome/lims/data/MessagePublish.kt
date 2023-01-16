package com.gcgenome.lims.data

class MessagePublish(
    val type: MessageType,
    val data: AlisResponse
){
    enum class  MessageType{
        _NULL, CREATE, PRINTING, FINISH
    }
}