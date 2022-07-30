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
        return Flux.fromIterable(worklist).flatMap { map(row, it) }.collectList()
            .map { analysis->
            val today = LocalDate.now()
            val title = "${formatter.format(today)}_${worklist.stream().map(Worklist::serial).distinct().collect(Collectors.joining("_"))}"
            Batch(idx=idx).also {
                it.title = title
                it.analysis.addAll(analysis)
                it.value[UUIDEnum.WORKFLOW.toUUID()] = "GenerateFASTQ"
                it.value[UUIDEnum.APPLICATION.toUUID()] = "FASTQ Only"
                it.value[UUIDEnum.CHEMISTRY.toUUID()] = "Amplicon"
                it.value[UUIDEnum.ASSAY.toUUID()] = "Swift"
                it.value[UUIDEnum.ADAPTOR1.toUUID()] = "AGATCGGAAGAGCACACGTCTGAACTCCAGTCA"
                it.value[UUIDEnum.ADAPTOR2.toUUID()] = "AGATCGGAAGAGCGTCGTGTAGGGAAAGAGTGT"
            }
        }
    }
    private fun map(row: AtomicInteger, worklist: Worklist): Flux<Analysis> = repo.findAllByWorklist(worklist.id)
        .sort(Comparator.comparing(Sequencing::index))
        .map {
            val serial = worklist.serial
            check(serial!=null)
            map(row.getAndIncrement(), serial, it)
        }
    private fun map(row: Int, worklistSerial: String, sequencing: Sequencing): Analysis {
        val samples = if(sequencing.samples!=null) sequencing.samples.split("ː") else emptyList()
        val services = if(sequencing.services!=null) sequencing.services.split("ː") else emptyList()
        val samplePickOne = samples.stream().filter(Objects::nonNull).filter(String::isNotBlank).findFirst().orElse(null)
        val servicePickOne = services.stream().filter(Objects::nonNull).filter(String::isNotBlank).findFirst().orElse(null)
        val serial = "$worklistSerial-${sequencing.index.toString().padStart(2, '0')}"
        val sort = (row*5).toString().padStart(4, '0')
        val seqname = if(samplePickOne!=null) {
            serial + "_" + samplePickOne.substring(0, 8) + "-" + samplePickOne.substring(8, 11) + "-" + samplePickOne.substring(11)
        } else serial + "_" + sequencing.gid
        return Analysis(row=row, patientId=samplePickOne?.toLong(), code=servicePickOne, serial=serial, sort=sort).apply {
            this.value[UUIDEnum.ORDER_NAME.toUUID()] = "-"
            this.value[UUIDEnum.PATIENT_NAME.toUUID()] = "-"
            this.value[UUIDEnum.TAT.toUUID()] = "-"
            this.value[UUIDEnum.ANALYSIS_NAME.toUUID()] = seqname
            this.value[UUIDEnum.GTRACKER.toUUID()] = sequencing.gid
            this.value[UUIDEnum.I7IDX.toUUID()] = sequencing.indexI7
            this.value[UUIDEnum.I7SEQ.toUUID()] = sequencing.sequenceI7
            this.value[UUIDEnum.I5IDX.toUUID()] = sequencing.indexI5
            this.value[UUIDEnum.I5SEQ.toUUID()] = sequencing.sequenceI5
        }
    }
}