package com.gcgenome.alis.service

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.gcgenome.alis.data.Request
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

@Service("ALIS-API")
class Procedure(private val databaseClient: DatabaseClient) {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    private val API_SET_UPLOAD_LAB_REGFILE_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
            "<soap12:Header>" +
            "<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
            "<UserName>{:username}</UserName>" +
            "<Password>{:password}</Password>" +
            "</AuthenticationHeader>" +
            "</soap12:Header>" +
            "<soap12:Body>" +
            "<SetUploadLabRegFile xmlns=\"http://tempuri.org/\">" +
            "<req>" +
            "<LabRegDate>{:date}</LabRegDate>" +
            "<LabRegNo>{:reqno}</LabRegNo>" +
            "<ReportCode>{:code}</ReportCode>" +
            "<TestCode>{:code}</TestCode>" +
            "<FileDisplayName>{:file-name}</FileDisplayName>" +
            "<FileExt>{:file-ext}</FileExt>" +
            "<FileSize>{:file-size}</FileSize>" +
            "<FileKind>{:file-type}</FileKind>" +
            "<FileBuffer>{:file-data}</FileBuffer>" +
            "<FileCreateTime>{:file-date}</FileCreateTime>" +
            "<FileDescription>{:file-desc}</FileDescription>" +
            "<TextReport><![CDATA[{:text-report}]]></TextReport>" +
            "<MemberID>{:member-id}</MemberID>" +
            "</req>" +
            "</SetUploadLabRegFile>" +
            "</soap12:Body>" +
            "</soap12:Envelope>"
    private val API_UPDATE_LAB_WORK_LIST = """<?xml version="1.0" encoding="utf-8"?>
<soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
  <soap12:Header>
    <AuthenticationHeader xmlns="http://tempuri.org/">
      <UserName>{:username}</UserName>
      <Password>{:password}</Password>
    </AuthenticationHeader>
  </soap12:Header>
  <soap12:Body>
    <UpdateLabWorkList xmlns="http://tempuri.org/">
      <labRegDate>{:date}</labRegDate>
      <labRegNo>{:reqno}</labRegNo>
      <errorMsg></errorMsg>
    </UpdateLabWorkList>
  </soap12:Body>
</soap12:Envelope>"""
    private val API_SET_REPORT_PUBLISH_CANCEL = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
            "<soap12:Header>" +
            "<AuthenticationHeader xmlns=\"http://tempuri.org/\">" +
            "<UserName>{:username}</UserName>" +
            "<Password>{:password}</Password>" +
            "</AuthenticationHeader>" +
            "</soap12:Header>" +
            "<soap12:Body>" +
            "<ReportPublishCancel xmlns=\"http://tempuri.org/\">" +
            "<labRegDate>{:date}</labRegDate>" +
            "<labRegNo>{:reqno}</labRegNo>" +
            "<reportCode>{:code}</reportCode>" +
            "<errorMsg></errorMsg>" +
            "</ReportPublishCancel>" +
            "</soap12:Body>" +
            "</soap12:Envelope>"

    private val DTF = DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MM-dd").toFormatter()

    @Value("\${alis.api.url}")
    private val url: String = ""

    @Value("\${alis.api.content-type}")
    private val contentType: String = ""

    @Value("\${alis.api.username}")
    private val username: String = ""

    @Value("\${alis.api.password}")
    private val password: String = ""
    private val OM = ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .setLocale(Locale.KOREA)
        .registerModule(JavaTimeModule())

    enum class FileType {
        GENERAL, PDF, JPG, JPG_PER_PAGE, ETC, TEXT_SHORTER, TEXT, JSON
    }

    /*report 파일 전송*/
    @Throws(IOException::class)
    fun fileUpload(
        user: String,
        request: Request,
        file: ByteArray,
        fileName: String,
        type: FileType,
        create: LocalDate,
        desc: String
    ): Boolean {
        val ext = if (type.ordinal == 1) "pdf" else "jpg"
        val size =  /*file.length();*/file.size.toLong()
        val data = Base64.getEncoder().encodeToString( /*Files.toByteArray(file)*/file)
        val xml: String =
            API_SET_UPLOAD_LAB_REGFILE_TEMPLATE.replace("{:username}", username)
                .replace("{:password}", password)
                .replace("{:date}",  DTF.format(request.requestDate))
                .replace("{:reqno}", java.lang.String.valueOf(request.requestNo))
                .replace("{:code}", request.itemCode)
                .replace("{:code}", request.itemCode)
                .replace("{:file-name}", fileName)
                .replace("{:file-ext}", ext)
                .replace("{:file-size}", size.toString())
                .replace("{:file-type}", type.ordinal.toString())
                .replace("{:file-date}", DTF.format(create))
                .replace("{:file-desc}", desc)
                .replace("{:file-data}", data)
                .replace("{:member-id}", user)
        val response: Document = Jsoup.connect(url).maxBodySize(0)
            .method(Connection.Method.POST)
            .header("content-type", contentType)
            .requestBody(xml)
            .parser(Parser.xmlParser())
            .execute().parse()
        val values: String = response.select("SetUploadLabRegFileResult").text().replace("\"\"", "null")
        val result: Boolean = OM.readValue<Boolean>(values, Boolean::class.java)
        log.info(
            request.requestDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + request.requestNo
                .toString() + ", " + request.itemCode.toString() + ": FileUpload(" + type.toString() + ")=>" + result
        )
        return result
    }

    /*체크리스트에서 워크리스트로 전송(워크리스트 전송 누락건 처리. 에러를 리턴하는 것이 정상이므로 결과를 반전하여 리턴함)*/
    @Throws(IOException::class)
    fun chkWorklist(request: Request): Boolean {
        val xml: String = API_UPDATE_LAB_WORK_LIST.replace("{:username}", username)
            .replace("{:password}", password)
            .replace("{:date}", DTF.format(request.requestDate))
            .replace("{:reqno}", java.lang.String.valueOf(request.requestNo))
        val response: Document = Jsoup.connect(url).maxBodySize(0)
            .method(Connection.Method.POST)
            .header("content-type", contentType)
            .requestBody(xml)
            .parser(Parser.xmlParser())
            .execute().parse()
        val values: String = response.select("UpdateLabWorkListResult").text().replace("\"\"", "null")
        val result: Boolean = OM.readValue(values, Boolean::class.java)
        if (result) try {
            val msg: String = response.select("errorMsg").text().replace("\"\"", "null")
            throw RuntimeException(msg)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(response.data())
        }
        return true
    }

    /*report 배포 취소(파일 삭제)*/
    @Throws(IOException::class)
    fun cancelPublish(request: Request): Boolean {
        val xml: String = API_SET_REPORT_PUBLISH_CANCEL.replace("{:username}", username)
            .replace("{:password}", password)
            .replace("{:date}", DTF.format(request.requestDate))
            .replace("{:reqno}", java.lang.String.valueOf(request.requestNo))
            .replace("{:code}", request.itemCode)
        val response: Document = Jsoup.connect(url).maxBodySize(0)
            .method(Connection.Method.POST)
            .header("content-type", contentType)
            .requestBody(xml)
            .parser(Parser.xmlParser())
            .execute().parse()
        val values: String = response.select("ReportPublishCancelResult").text().replace("\"\"", "null")
        val result: Boolean = OM.readValue<Boolean>(values, Boolean::class.java)
        if (!result) try {
            val msg: String = response.select("errorMsg").text().replace("\"\"", "null")
            log.info(msg)
            throw RuntimeException(msg)
        } catch (e: Exception) {
            throw RuntimeException(response.data())
        }
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return true
    }


    fun state(request: Request, state: String, member: String?, machine: String?): Mono<Boolean> {
        val query = "exec Interface_SetPatientTestState :1 :2 :3 :4 :5 :6 :7"
        return databaseClient.sql(query)
            .bind("1", request.requestDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .bind("2", request.requestNo)
            .bind("3", request.itemCode)
            .bind("4", request.itemCode)
            .bind("5", state)
            .bind("6", member)
            .bind("7", machine)
            .fetch().first().map { it.values.toList().first() as Boolean }
    }
}