package com.greencross.lims.report.builder

import com.gcgenome.lims.report.TextBlock
import com.greencross.lims.report.cancerch.CancerchDto
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSFloat
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.util.Matrix
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


open class Util {
    companion object {
        private val DTF: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        fun date(date: LocalDate?): String? {
            return if (date == null) null else DTF.format(date)
        }
        fun dateOrDash(date: LocalDate?): String {
            return if (date == null) "-" else DTF.format(date)
        }
        fun getDynamicFontSize(maxSize: Float = 10f, minSize: Float = 0f, textLength: Int, maxLength: Int = 9999): Float {
            return if (textLength > maxLength) {
                minSize
            } else {
                maxSize - (textLength / maxLength.toFloat()) * (maxSize - minSize)
            }
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
                Sex.M -> "남"
                Sex.F -> "여"
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
            HEADER_WIDTH: Float, //595
            HEADER_HEIGHT: Float //100
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
            CancerchDto.Results.GENERAL -> "일반관리"
            CancerchDto.Results.CONCERN -> "관심관리"
            else -> "집중관리"
        }
        fun lblCancerToWord(cancer: String) = when (cancer) {
            "기타암종" -> "기타 암"
            else -> cancer
        }
        fun lblPatientSir(name: String) = "${name}님"
        fun lblPatientInfo(age: String, sex: Sex): String {
            val ageStream: String = (age.toInt() / 10 * 10).toString()
            val cut: String = when {
                age.substring(age.length - 1, age.length).toInt() >= 5 -> "후반"
                else -> "초반"
            }
            val sexStr: String = when {
                sex == Sex.F -> "여성"
                else -> "남성"
            }
            return ageStream + "대 " + cut + " " + sexStr + " 평균"
        }
        fun lblPatientInfoWithCancer(age: String, sex: Sex, cancer: String): String {
            val ageStream: String = (age.toInt() / 10 * 10).toString()
            val cut: String = when {
                age.substring(age.length - 1, age.length).toInt() >= 5 -> "후반"
                else -> "초반"
            }
            val sexStr: String = when {
                sex == Sex.F -> "여성"
                else -> "남성"
            }
            val canStr: String = when (cancer) {
                "기타암종" -> "전체 암"
                else -> cancer
            }
            return ageStream + "대 " + cut + " " + sexStr + " " + canStr
        }
    }
}
