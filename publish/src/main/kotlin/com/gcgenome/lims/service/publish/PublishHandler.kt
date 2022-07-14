package com.gcgenome.lims.service.publish

import com.gcgenome.alis.Client
import com.gcgenome.alis.FileUpload
import com.gcgenome.alis.Request
import com.gcgenome.lims.service.report.ReportDao
import com.gcgenome.lims.service.reportfile.ReportFileRepository
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.json.JSONObject
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO

@Service
class PublishHandler(
    val reportDao: ReportDao,
    val reportFileRepo: ReportFileRepository,
    val client: Client
    ) {
    @Transactional
    fun publish(sample: Long, service: String, createAt: Long) : Mono<Boolean> {
        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            .publishOn(Schedulers.boundedElastic())
            .flatMap {
                val sampleId = it.sample
                val requestNum = (it.sample % 10000000).toInt()
                val requestDate =
                    LocalDate.parse(String.format("%d", sampleId / 10000000), DateTimeFormatter.ofPattern("yyyyMMdd"))
                val requestAlis = Request(requestDate, requestNum, it.service)
                val data = reportFileRepo.findById(it.file).map { it2 ->
                    it2.data!!.array()
                }.get()
                val jsonObject = JSONObject().put("Data", data)
                val worklist = client.chkWorklist(requestAlis)
                val cancel = client.cancelPublish(requestAlis)
                jsonObject.put("worklist", worklist)
                jsonObject.put("cancelPublish", cancel)

                getUser().flatMap{ user ->
                    client.state(requestAlis, "F", user.authentication.principal.toString(), "LIMS")
                }.filter{result -> result}
                .zipWith(getUser())
                .map { tuple -> createImgDiv(tuple.t2.authentication.principal.toString(), data, requestAlis) }
                .filter{result -> result}
                .zipWith(getUser())
                .map { tuple -> sendToAlis(tuple.t2.authentication.principal.toString(), data, requestAlis, "pdf", "") }
                .filter{result -> result}
                .zipWith(getUser())
                .flatMap { tuple ->
                    client.state(requestAlis, "I", tuple.t2.authentication.principal.toString(), "LIMS")
                }.flatMap{ _ ->
                    reportDao.merge(it.sample, it.service, it.createAt, jsonObject.toString())
                }
                .then(Mono.just(true))
                .switchIfEmpty(Mono.just(false))
            }

    }
    private fun sendToAlis(user: String, data: ByteArray, request: Request, flg: String, page: String) : Boolean {
        val prefix = request.requestDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val infix  = String.format("%d", request.requestNo/10000)
        val suffix = String.format("%d", request.requestNo%10000)
        val title = "${prefix}_${infix}${suffix}"
        if("pdf" == flg)           return client.fileUpload(FileUpload(user, request, data, title, "PDF", LocalDate.now(), ""))
        else if("imgDiv" == flg)   return client.fileUpload(FileUpload(user, request, data, title + "_" + page, "JPG_PER_PAGE", LocalDate.now(), ""))
        else if("imgTotal" == flg) return client.fileUpload(FileUpload(user, request, data, title, "JPG", LocalDate.now(), ""))
        return false
    }
    private fun createImgDiv(user: String, data: ByteArray, request: Request) : Boolean {
        val doc: PDDocument = PDDocument.load(data)
        val renderer= PDFRenderer(doc)
        for (page in 0 until doc.numberOfPages) {
            val img: BufferedImage = renderer.renderImageWithDPI(page, 93f, ImageType.RGB)
            val baos = ByteArrayOutputStream()
            ImageIO.write(img, "jpg", baos)
            baos.flush()
            baos.close()
            val imageByte = baos.toByteArray()
            if (!sendToAlis(user, imageByte, request, "imgDiv", page.toString())) return false
        }
        return true
    }
    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }
}