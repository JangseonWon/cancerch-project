package com.greencross.lims.report.builder

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.cancerch.CancerchDto
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSFloat
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.util.Matrix
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


open class Util_EnUS {
    companion object {
        private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        fun date(date: LocalDate?): String? {
            return if (date == null) null else DTF.format(date)
        }
        fun age(birth: LocalDate?, sampling: LocalDate?): String {
            if (birth == null) return "-"
            return if (sampling == null) (Period.between(
                birth,
                LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
            ).years + 1).toString() else (Period.between(
                birth,
                sampling.with(TemporalAdjusters.firstDayOfYear())
            ).years + 1).toString()
        }

        fun sex(sex: Sex?): String {
            return if (sex == null) "-" else when (sex) {
                Sex.M -> "M"
                Sex.F -> "F"
            }
        }
        fun dashIfEmpty(str: String): String {
            if (str.trim().isEmpty()) return "-"
            else return str.trim()
        }

        fun icon(
            stream: PDPageContentStreamPageAccessible,
            icon: PDImageXObject,
            x: Float,
            y: Float,
            HEADER_WIDTH: Float,
            HEADER_HEIGHT: Float
        ) {
            val scaleHeight = icon.height * HEADER_WIDTH / icon.width
            if (scaleHeight <= HEADER_HEIGHT) stream.drawImage(icon, x, y - scaleHeight, HEADER_WIDTH, scaleHeight)
            else {
                val scaleWidth = icon.width * HEADER_HEIGHT / icon.height
                stream.drawImage(icon, x, y - HEADER_HEIGHT, scaleWidth, HEADER_HEIGHT)
            }
        }

        fun italic(stream: PDPageContentStreamPageAccessible, x: Float, y: Float, text: TextBlock) {
            stream.saveGraphicsState();
            stream.beginText();
            val italicTransformMatrix = COSArray()
            italicTransformMatrix.add(COSInteger.get(1))
            italicTransformMatrix.add(COSInteger.get(0))
            italicTransformMatrix.add(COSFloat.get("0.2"))
            italicTransformMatrix.add(COSInteger.get(1))
            italicTransformMatrix.add(COSInteger.get(0))
            italicTransformMatrix.add(COSInteger.get(1))
            stream.setNonStrokingColor(text.style().color())
            stream.setTextMatrix(
                Matrix.concatenate(
                    Matrix.getTranslateInstance(x, y),
                    Matrix.createMatrix(italicTransformMatrix)
                )
            )
            stream.setFont(text.style().fontMostSuitable(text.text()), text.style().fontSize())
            stream.showText(text.text())
            stream.endText()
            stream.restoreGraphicsState()
        }
        fun lblResultToWord(result: CancerchDto.Results) = when (result) {
            CancerchDto.Results.GENERAL -> "General Risk"
            CancerchDto.Results.CONCERN -> "Intermediate Risk"
            else -> "High Risk"
        }
        fun cancerToEng(cancer: String) = when (cancer) {
            "폐암" -> "Lung Cancer"
            "대장암" -> "Colon Cancer"
            "간암" -> "Liver Cancer"
            "췌장담도암" -> "Pancreatobiliary Cancer"
            "식도암" -> "Esophageal Cancer"
            "난소암" -> "Ovarian Cancer"
            else -> "Others"
        }
        fun lblPatientInfo(age: String, sex: Sex): String {
            val ageStream: String = (age.toInt() / 10 * 10).toString()
            val cut: String = when {
                age.substring(age.length - 1, age.length).toInt() >= 5 -> "in late "
                else -> "in early "
            }
            val sexStr: String = when {
                sex == Sex.F -> "Female "
                else -> "Male "
            }
            return sexStr + cut + ageStream +"s"
        }
        fun lblPatientInfoWithCancer(age: String, sex: Sex, cancer: String): String {
            val ageStream: String = (age.toInt() / 10 * 10).toString()
            val cut: String = when {
                age.substring(age.length - 1, age.length).toInt() >= 5 -> "late "
                else -> "early "
            }
            val sexStr: String = when {
                sex == Sex.F -> "Female "
                else -> "Male "
            }
            return when(cancer) {
                "기타암종" -> cut + ageStream +"s " + sexStr
                else -> sexStr + cancerToEng(cancer) + " patients in their "+ cut + ageStream + "s "
            }

        }
    }
}
