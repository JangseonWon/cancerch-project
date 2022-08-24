package com.gcgenome.lims.service.lims1

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object SampleSheetFactory {
    fun validation(analysis: List<Analysis>) {
        for(a in analysis) {
            if (a.value[UUIDEnum.I7SEQ.toUUID()] == null || a.value[UUIDEnum.I5SEQ.toUUID()] == null)
                throw RuntimeException("인덱스가 모두 채워지지 않았습니다.")
        }
        val length1 = analysis.stream().findFirst().get().value[UUIDEnum.I7SEQ.toUUID()]?.length
        val length2 = analysis.stream().findFirst().get().value[UUIDEnum.I5SEQ.toUUID()]?.length
        for(a in analysis) {
            if (a.value[UUIDEnum.I7SEQ.toUUID()]!!.length != length1 || a.value[UUIDEnum.I5SEQ.toUUID()]!!.length != length2)
                throw RuntimeException("인덱스 길이가 다릅니다.")
        }
        for(a in analysis) {
            val seq1 = a.value[UUIDEnum.I7SEQ.toUUID()] + a.value[UUIDEnum.I5SEQ.toUUID()]
            for(b in analysis) {
                if(a == b) continue
                val seq2 = b.value[UUIDEnum.I7SEQ.toUUID()] + b.value[UUIDEnum.I5SEQ.toUUID()]
                if(seq1 == seq2) throw RuntimeException("동일한 인덱스 쌍이 발견되었습니다:$seq1")
                val distance = mismatch(seq1, seq2)
                if(distance < 2) throw RuntimeException("유사한 인덱스 쌍이 발견되었습니다:$seq1/$seq2")
            }
        }
    }
    private fun mismatch(a: String, b: String): Int {
        if (a.length != b.length) throw RuntimeException()
        var mis = 0
        val cha = a.toCharArray()
        val chb = b.toCharArray()
        for (i in 0 until a.length) if (cha[i] != chb[i]) ++mis
        return mis
    }
    fun toSampleSheet(batch: Batch): String {
        val sb = StringBuilder()
        sb.append("[Header]").append("\r\n")
            .append("IEMFileVersion,4").append("\r\n")
            .append("Investigator Name,") /*.append(dto.getUser()!=null?dto.getUser():"")*/.append("\r\n")
            .append("Experiment Name,").append(batch.title).append("\r\n")
            .append("Date,").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .append("\r\n")
            .append("Workflow,").append(batch.value[UUIDEnum.WORKFLOW.toUUID()]).append("\r\n")
            .append("Application,").append(batch.value[UUIDEnum.APPLICATION.toUUID()]).append("\r\n")
            .append("Assay,").append(batch.value[UUIDEnum.ASSAY.toUUID()]).append("\r\n")
            .append("Description,").append("\r\n")
            .append("Chemistry,").append(batch.value[UUIDEnum.CHEMISTRY.toUUID()]).append("\r\n")
            .append("\r\n")

        // 어댑터 시퀀스 없이 돌린다고 함
        /*sb.append("[Settings]").append("\r\n")
            .append("Adapter,").append("AGATCGGAAGAGCACACGTCTGAACTCCAGTCA").append("\r\n")
            .append("AdapterRead2,").append("AGATCGGAAGAGCGTCGTGTAGGGAAAGAGTGT").append("\r\n")
            .append("\r\n")*/
        sb.append("[Data]").append("\r\n")
            .append("Sample_ID,Sample_Name,Sample_Plate,Sample_Well,I7_Index_ID,index,I5_Index_ID,index2,Sample_Project,Description").append("\r\n")

        batch.analysis.stream()
            .sorted { a, b -> a.sort.toInt().compareTo(b.sort.toInt()) }
            .forEach { r ->
                val row = StringBuilder()
                val sid = r.serial.trim()
                val sn = r.value[UUIDEnum.ANALYSIS_NAME.toUUID()]?.trim()
                val i7 = r.value[UUIDEnum.I7IDX.toUUID()]?.trim()
                val s7 = r.value[UUIDEnum.I7SEQ.toUUID()]?.trim()
                val i5 = r.value[UUIDEnum.I5IDX.toUUID()]?.trim()
                val s5 = r.value[UUIDEnum.I5SEQ.toUUID()]?.trim()
                row.append(sid).append(",").append(sn).append(",,,")
                    .append(i7).append(",").append(s7).append(",")
                    .append(i5).append(",").append(s5).append(",")
                    .append(",").append("${r.patientId}-${r.code}")
                sb.append(row.toString()).append("\r\n")
            }
        return sb.toString()
    }
}