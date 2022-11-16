package com.greencross.lims.service.report

import com.gcgenome.lims.data.MessageReport
import com.gcgenome.lims.data.Report
import org.springframework.stereotype.Service

@Service
class MessageHandler {
    private val queue = mutableListOf<MessageReport>()
    private val checkSet = mutableSetOf<Report>()

    fun consumeMessageAndGetQueue(message : MessageReport): List<MessageReport>{
        handleMessage(message)
        return queue.sortedBy { it.data.createAt() }
    }

    @Synchronized
    private fun handleMessage(message : MessageReport){
        when (message.type) {
            MessageReport.MessageType.CREATE -> creatingMessage(message)
            MessageReport.MessageType.PRINTING -> printingMessage(message)
            MessageReport.MessageType.FINISH -> finishMessage(message)
            MessageReport.MessageType._NULL -> throw RuntimeException("메시지 타임 null은 대체 뭐죠?")
        }
    }
    private fun creatingMessage(message: MessageReport) {
        if(checkSet.contains(message.data)) throw RuntimeException("이미 존재하는 메시지에 Create이 두번 실행?")
        checkSet.add(message.data)
        queue.add(message)
    }

    private fun printingMessage(message: MessageReport) {
        if(!checkSet.contains(message.data)) throw RuntimeException("존재하지 않은 메시지인데 Printing?")
        if(!queue.removeIf{message.data == it.data}) throw RuntimeException("존재하지 않은 메시지인데 Printing?")
        queue.add(message)
    }

    private fun finishMessage(message: MessageReport) {
        if(!checkSet.contains(message.data)) throw RuntimeException("존재하지 않은 메시지인데 Finish?")
        checkSet.remove(message.data)
        queue.removeIf{message.data == it.data}
    }

}