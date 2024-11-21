package com.greencross.lims.report

import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.TextStyle
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.oned.Code128Writer
import com.greencross.lims.report.builder.AbstractReportDto
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.AlignVertical
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.awt.Color
import java.io.IOException

class SectionBarcode<T : Template<*>, D : AbstractReportDto> :
    Painter<T, D> {
    @Throws(IOException::class)
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: T,
        dto: D
    ): PDPageContentStreamPageAccessible {
        if (dto.barcode != null && dto.barcode!!.trim().isNotEmpty()) {
            stream.saveGraphicsState()
            val barcodeWriter = Code128Writer()
            val bitMatrix = barcodeWriter.encode(dto.barcode, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_HEIGHT)
            val barcodeImg = LosslessFactory.createFromImage(
                template.resource().doc(),
                MatrixToImageWriter.toBufferedImage(bitMatrix)
            )
            val ts = TextStyle().color(Color.decode("#808080")).fontSize(5f).justify(true).paragraph(false)
            stream.drawImage(
                barcodeImg,
                BARCODE_POS_X,
                BARCODE_POS_Y + 5,
                BARCODE_WIDTH.toFloat(),
                BARCODE_HEIGHT.toFloat()
            )
                .paragraph(
                    BARCODE_POS_X + 2,
                    BARCODE_POS_Y,
                    (BARCODE_WIDTH - 4).toFloat(),
                    AlignHorizontal.JUSTIFY,
                    AlignVertical.MIDDLE,
                    TextBlock(ts, dto.barcode)
                )
            stream.restoreGraphicsState()
        }
        return stream
    }

    companion object {
        private const val BARCODE_WIDTH = 100
        private const val BARCODE_HEIGHT = 20
        private const val BARCODE_POS_X = 440f
        private const val BARCODE_POS_Y = 805f
    }
}
