package com.gcgenome.alis.service

import com.gcgenome.alis.data.FileUpload
import com.gcgenome.alis.data.Request
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDate

@Service
class Handler(private val procedure: Procedure) {
    fun state(request: Request, state: String, member: String, machine: String) = procedure.state(request, LocalDate.parse(state), member.toLong(), machine)
    fun fileUpload(file : FileUpload) = procedure.fileUpload(file.user, file.request, file.data, file.title, file.fileType, file.create,file.desc)
    fun cancelPublish(request: Request) = procedure.cancelPublish(request)
    fun chkWorklist(request: Request) = procedure.cancelPublish(request)

}