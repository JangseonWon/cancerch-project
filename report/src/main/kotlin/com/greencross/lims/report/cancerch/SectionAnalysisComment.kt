package com.greencross.lims.report.cancerch

import com.greencross.lims.report.func.PDPageContentStreamPageAccessible
import com.greencross.lims.report.func.Painter
import java.awt.Color

class SectionAnalysisComment(private val y: Float = 480f) : Painter<CancerchTemplate<CancerchResource>, CancerchDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible?,
        template: CancerchTemplate<CancerchResource>?,
        dto: CancerchDto?
    ): PDPageContentStreamPageAccessible {
        stream!!.saveGraphicsState()
        var y = 0f
        if (dto!!.result != CancerchDto.Results.RISK) {
            y = this.y - RESULT_IMAGE_COMMENT_RATE + 85
            val img = template!!.resource().imgAnalysisContentBox()
            val width = img.width * RESULT_IMAGE_COMMENT_RATE / img.height
            stream.drawImage(img, 297f - width / 2, y, width, RESULT_IMAGE_COMMENT_RATE - 95)
        } else {
            if (dto.first.name != "기타암종") {
                y = this.y - RESULT_IMAGE_COMMENT_RATE + 60
                val img = template!!.resource().imgAnalysisContentBox()
                val width = img.width * RESULT_IMAGE_COMMENT_RATE / img.height
                stream.drawImage(img, 297f - width / 2, y, width, RESULT_IMAGE_COMMENT_RATE - 80)
            }
        }

        if (CancerchDto.Results.GENERAL == dto.result) {
            template!!.lblDetailResultAnalysisGeneral(stream, y, dto)
        } else if (CancerchDto.Results.CONCERN == dto.result)
            template!!.lblDetailResultAnalysisConcern(stream, y, dto)
        else {
            if (dto.first.name != "기타암종") template!!.lblDetailResultAnalysisCancer(stream, y, dto)

            else {
                stream.line(47f,493f, 542f, 493f).setLineWidth(0.4f).setStrokingColor(Color.BLACK).stroke()
                template!!.lblDetailResultAnalysisOthers(stream, y, dto)
            }
        }
        return stream
    }

    companion object {
        private const val RESULT_IMAGE_COMMENT_RATE = 201f
    }
}