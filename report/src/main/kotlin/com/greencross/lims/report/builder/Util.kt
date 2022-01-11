package com.greencross.lims.report.builder

import com.greencross.lims.report.TextBlock
import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSFloat
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.util.Matrix


open class Util {
    companion object {
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
    }
}