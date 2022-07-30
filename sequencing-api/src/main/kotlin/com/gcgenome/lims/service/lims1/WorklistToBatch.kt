package com.gcgenome.lims.service.lims1

import com.gcgenome.lims.entity.Sequencing
import com.gcgenome.lims.entity.Worklist
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

@Component
class WorklistToBatch(val repo: SequencingRepository) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")
    fun map(idx: Int, worklist: List<Worklist>): Mono<Batch> {
        val row = AtomicInteger(1)
        return Flux.fromStream(worklist.stream().distinct()).flatMap { map(row, it) }
            .collectList().map { analysis->
                val today = LocalDate.now()
                val title = "${formatter.format(today)}_${worklist.stream().map(Worklist::serial).distinct().collect(Collectors.joining("_"))}"
                Batch(idx=idx).also {
                    it.title = title
                    it.analysis.addAll(analysis)
                }
            }
    }
    private fun map(row: AtomicInteger, worklist: Worklist): Flux<Analysis> = repo.findAllByWorklist(worklist.id).map {
        val title = worklist.title
        check(title!=null)
        map(row.getAndIncrement(), title, it)
    }
    private fun map(row: Int, worklistTitle: String, sequencing: Sequencing): Analysis {
        val samples = sequencing.samples.split(":")
        val services = sequencing.services.split(":")
        val samplePickOne = samples.stream().filter(Objects::nonNull).filter(String::isNotBlank).findFirst().orElse(null)
        val servicePickOne = services.stream().filter(Objects::nonNull).filter(String::isNotBlank).findFirst().orElse(null)
        val serial = "$worklistTitle-${sequencing.index.toString().padStart(2, '0')}"
        val sort = (row*5).toString().padStart(4, '0')
        val seqname = serial + "_" + samplePickOne.substring(0, 8) + "-" + samplePickOne.substring(8, 11) + "-" + samplePickOne.substring(11)
        return Analysis(row=row, patientId=samplePickOne.toLong(), code=servicePickOne, serial=serial, sort=sort).apply {
            this.value[SEQUENCING_ORDER_NAME] = "-"
            this.value[SEQUENCING_PATIENT_NAME] = "-"
            this.value[SEQUENCING_TAT] = "-"
            this.value[SEQUENCING_ANALYSIS_NAME] = seqname
            this.value[SEQUENCING_STB] = sequencing.gid
        }
    }
    private val SEQUENCING_ANALYSIS_NAME = UUID.fromString("f46136d7-7cfc-4f79-adb0-c254edd5c72a")
    private val SEQUENCING_ORDER_NAME = UUID.fromString("494aa18c-681b-4509-b3b4-b5166682fc7a")
    private val SEQUENCING_PATIENT_NAME = UUID.fromString("5e5e7085-c856-4daa-a866-76ec1218eacf")
    private val SEQUENCING_TAT = UUID.fromString("47b1714e-ba4e-4cc2-aa5c-69091b34e2f1")
    private val SEQUENCING_STB = UUID.fromString("68a8dff2-1852-4f5e-8449-635961b29092")
}