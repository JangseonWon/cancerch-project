package com.greencross.lims.report.ON204.enus.resource

import com.gcgenome.lims.report.Template
import com.gcgenome.lims.report.TextStyle
import com.greencross.lims.report.ON204.DNACXDto
import com.greencross.lims.report.ON204.resource.DNACXResource
import com.greencross.lims.report.HasSign
import com.greencross.lims.report.builder.AbstractReportDto
import com.greencross.lims.report.enus.HasHeaderEnUs
import com.greencross.lims.report.enus.HasSignEnUs
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color
import java.io.File

abstract class DNACXResourceEnUs(doc: PDDocument): DNACXResource, HasSignEnUs, HasHeaderEnUs {
    var doc                     : PDDocument = PDDocument()

    override fun doc(): PDDocument {
        return doc
    }
    //검사자/전문의 서명
    override fun labels(): Array<HasSign.SignLabel> {
        return arrayOf(
            object : HasSign.SignLabel {
                override fun label(): String { return "Tested By: " }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<HasSign.Person?> {
                    return arrayOf(person("김다솜"))
                }
            },
            object : HasSign.SignLabel {
                override fun label(): String { return "Reported by/Reviewed by: " }
                override fun persons(template: Template<*>, dto: AbstractReportDto): Array<HasSign.Person?> {
                    return arrayOf(person("조은해"), person("설창안"))
                }
            }
        )
    }

    //이미지 로드
    override fun imgHuman(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/human.png"))
    override fun imgHumanBox(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/humanBox.png"))
    override fun imgSummaryOfResults(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionSummaryOfResults/table.png"))
    override fun imgCangerTypeIcon(cancer: String, type: Boolean): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/${cancer}_${type}.png"))
    override fun imgCancerBox(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/cancerBox.png"))
    override fun imgCancerTypeImage(cancer: String): PDImageXObject  = img(File(DNACXResource.resource, "img/avoid/SectionTissueOfOrigin/${cancer}.png"))
    override fun lblCancerName(cancer: String): String = when(cancer) {
        "폐암" -> "Lung Cancer"
        "간암" -> "Liver Cancer"
        "대장암" -> "Colon Cancer"
        "식도암" -> "Esophageal Cancer"
        "췌장담도암" -> "Pancreatic Cancer"
        "난소암" -> "Ovarian Cancer"
        else -> throw Exception("정의되지 않은 암종이 등록됐음")
    }

    private fun toCancerNameWithoutCancer(cancer: String): String = when(cancer) {
        "폐암" -> "Lung"
        "간암" -> "Liver"
        "대장암" -> "Colon"
        "식도암" -> "Esophageal"
        "췌장담도암" -> "Pancreatic"
        "난소암" -> "Ovarian"
        else -> throw Exception("정의되지 않은 암종이 등록됐음")
    }

    override fun lblInterpretationBold(dto: DNACXDto): String = when {
        dto.risk == DNACXDto.Risk.HIGH && dto.result.signalScore99CutOff <= dto.result.signalScore -> "The test result is high risk and the DNA pattern is most similar to that of [ ${toCancerNameWithoutCancer(dto.result.cancer)} ] cancer patients."
        dto.risk == DNACXDto.Risk.HIGH -> "The test result is high risk and the DNA pattern is significantly similar to that of cancer patients."
        dto.risk == DNACXDto.Risk.MODERATE -> "The test result is moderate risk and the DNA pattern is moderately similar to that of cancer patients."
        dto.risk == DNACXDto.Risk.MILD -> "The test result is mild risk and the DNA pattern is slightly different from that of healthy individuals."
        dto.risk == DNACXDto.Risk.LOW -> "The test result is low risk and the DNA pattern is similar to that of healthy individuals."
        else -> throw Exception("소견 자동생성 불가")
    }
    private fun buildParagraph(risk: DNACXDto.Risk, ssIsIncreased95: Boolean, ssIsIncreased99: Boolean, covIsIncreased: Boolean, femsIsIncreased: Boolean, cfDNAIsIncreased: Boolean, genomicIsIncreased: Boolean): String {
        return when {
            //양성 0개
            risk == DNACXDto.Risk.LOW       && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "No abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score, cfDNA concentration and genomic instability (i-score) showed no significant increase.\n" + "Based on cfDNA analysis performed using an artificial intelligence algorithm, this result indicates a low likelihood of cancer presence.\n" + "\n" + "The ai-CANCERCH test cannot detect all types of cancer, and its detection performance may vary depending on the cancer's type or stage.\n" + "\n" + "This test is a cancer screening test, not a diagnostic test, so a physician's diagnosis is recommended."
            //양성 1개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: the Signal score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probabilities (COV, FEMS score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A moderate risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at moderate risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: the Signal score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probabilities (COV, FEMS score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: the COV score showed an increase. However, no increase was observed in other tumor signal probabilities (Signal, FEMS score), cfDNA concentration and genomic instability (i-score).\n" + "\n" + "Based on cfDNA analysis performed using an artificial intelligence algorithm, this result reveals a slightly different DNA pattern.\n" + "\n" + "Since this result reflects only the current condition, follow-up and monitoring are recommended."
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: the FEMS score showed an increase. However, no increase was observed in other tumor signal probabilities (Signal, COV score), cfDNA concentration and genomic instability (i-score).\n" + "\n" + "Based on cfDNA analysis performed using an artificial intelligence algorithm, this result reveals a slightly different DNA pattern.\n" + "\n" + "Since this result reflects only the current condition, follow-up and monitoring are recommended."
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: cfDNA concentration showed an increase. However, no increase was observed in tumor signal probabilities (Signal, COV, FEMS score) and genomic instability (i-score).\n" + "\n" + "Based on cfDNA analysis performed using an artificial intelligence algorithm, this result reveals a slightly different DNA pattern.\n" + "\n" + "Since this result reflects only the current condition, follow-up and monitoring are recommended."
            risk == DNACXDto.Risk.MILD      && !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal pattern was detected in the ai-CANCERCH TEST: genomic instability (i-score) showed an increase. However, no increase was observed in tumor signal probabilities (Signal, COV, FEMS score) and cfDNA concentration.\n" + "\n" + "Based on cfDNA analysis performed using an artificial intelligence algorithm, this result reveals a slightly different DNA pattern.\n" + "\n" + "Since this result reflects only the current condition, follow-up and monitoring are recommended."
            //양성 2개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: the Signal and COV score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probability (FEMS score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A moderate risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at moderate risk depending on their health conditions(e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: the Signal and COV score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probability (FEMS score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: the Signal and FEMS score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probability (COV score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A moderate risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at moderate risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: the Signal and FEMS score showed an increase among the tumor signal probabilities. However, no increase was observed in other tumor signal probability (COV score), cfDNA concentration and genomic instability (i-score)\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probabilities (COV, FEMS score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probabilities (COV, FEMS score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
                                                ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            risk == DNACXDto.Risk.HIGH      && !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: COV score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probabilities(Signal, FEMS score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions(e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 3개
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and FEMS score showed an increase. However, no increase was observed in cfDNA concentration and genomic instability (i-score).\n" + "\n" + "A moderate risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at moderate risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and FEMS score showed an increase. However, no increase was observed in cfDNA concentration and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probability (FEMS score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probability (FEMS score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (FEMS score) and cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (FEMS score) and cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.MODERATE  &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probability (COV score) and genomic instability (i-score).\n" + "\n" + "A moderate risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at moderate risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score and cfDNA concentration showed an increase. However, no increase was observed in other tumor signal probability (COV score) and genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (COV score) and cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (COV score) and cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
                                                ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                                ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
                                               !ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 4개
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score and genomic instability (i-score) showed an increase. However, no increase was observed in cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score and genomic instability (i-score) showed an increase. However, no increase was observed in cfDNA concentration.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score and cfDNA concentration showed an increase. However, no increase was observed in genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased && !cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score and cfDNA concentration showed an increase. However, no increase was observed in genomic instability (i-score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, cfDNA concentration and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (FEMS score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased && !femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, cfDNA concentration and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (FEMS score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score, cfDNA concentration and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (COV score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 && !covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, FEMS score, cfDNA concentration and genomic instability (i-score) showed an increase. However, no increase was observed in other tumor signal probability (COV score).\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
                                               !ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased && !genomicIsIncreased -> throw Exception("판독안된 케이스로 소견생성 불가")
            //양성 5개
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 && !ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score, cfDNA concentration and genomic instability (i-score) showed an increase.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 5% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            risk == DNACXDto.Risk.HIGH      &&  ssIsIncreased95 &&  ssIsIncreased99 &&  covIsIncreased &&  femsIsIncreased &&  cfDNAIsIncreased &&  genomicIsIncreased -> "Abnormal patterns were detected in the ai-CANCERCH TEST: Signal score, COV score, FEMS score, cfDNA concentration and genomic instability (i-score) showed an increase.\n" + "\n" + "A high risk does not imply a cancer diagnosis. Even 1% of healthy individuals may be classified as at high risk depending on their health conditions (e.g. benign diseases, autoimmune diseases).\n" + "\n" + "Note: Consultation with the physician for follow-ups is recommended."
            else -> throw Exception("분류되지 않은 케이스로 소견 문단 2번 생성이 불가능합니다. Risk: ${risk} ss95: ${ssIsIncreased95} ss99: ${ssIsIncreased99} cov: ${covIsIncreased} fems: ${femsIsIncreased} cfDNA: ${cfDNAIsIncreased} i-score: ${genomicIsIncreased}")
        }
    }
    override fun lblInterpretationRegular(dto: DNACXDto): String {
        val ScoreParagraph = buildParagraph(
            dto.risk,
            dto.result.signalScore >= dto.result.signalScore95CutOff,
            dto.result.signalScore >= dto.result.signalScore99CutOff,
            dto.result.covScore >= dto.result.covScoreCutOff,
            dto.result.femsScore >= dto.result.femsScoreCutOff,
            dto.result.cfDNAConcentration >= dto.result.cfDNAConcentrationCutOff,
            dto.result.genomicInstability >= dto.result.genomicInstabilityCutOff)
        return ScoreParagraph
    }

    override fun imgSignalScoreBackground(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/signal_en.png"))
    override fun imgShortSmallTitle(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/shortSmallTitle.png"))
    override fun imgLongSmallTitle(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/LongSmallTitle.png"))
    override fun imgCovScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/cov_en.png"))
    override fun imgFemsScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/fems_en.png"))
    override fun imgGenomicInstabilityScoreBackgorund(): PDImageXObject = img(File(DNACXResource.resource, "img/avoid/SectionScores/iscore_en.png"))
    //region #Override Colors
    override fun colorGray(): Color { return Color.decode("0xEFEFEF") }
    override fun colorText(): Color { return super.colorText()             }
    //endregion

    //region #Override Fonts
    override fun fontHeaderValue():         PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound08.ttf"))
    override fun fontHeader():              PDFont = font(File(DNACXResource.resource, "font/SDGothicNeoRound06.ttf"))
    override fun fontTitle():               PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound01.ttf"))
    override fun fontText():                PDFont = font(File(DNACXResource.resource, "font/SdGothicNeoRound04.ttf"))
    override fun fontScientific():          PDFont = font(File(DNACXResource.resource, "font/NanumBarunGothic.ttf"))
    override fun fontContentRegular():      PDFont = font(File(DNACXResource.resource, "font/MyriadPro-Regular.ttf"))
    override fun fontContentBold():         PDFont = font(File(DNACXResource.resource, "font/MyriadPro-Bold.ttf"))
    override fun fontSpecial():             PDFont = font(File(DNACXResource.resource, "font/Sandoll_격동고딕2_TTF_05_Bd.ttf"))
    override fun styleContentRegualar():    TextStyle = TextStyle().color(Color(35,24, 21)).fontSize(8f).fonts(fontContentRegular())
    override fun styleContentBold():        TextStyle = TextStyle().color(Color(67,72,142)).fontSize(8f).fonts(fontContentBold())
    override fun styleContentSpecial():     TextStyle = TextStyle().fonts(fontSpecial())
    //endregion

}
