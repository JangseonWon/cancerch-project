package com.greencross.lims.report.ON206.jajp.component

import com.gcgenome.lims.report.TextBlock
import com.gcgenome.lims.report.func.AlignHorizontal
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible
import com.gcgenome.lims.report.func.Painter
import com.greencross.lims.report.ON206.DNACTDto
import com.greencross.lims.report.ON206.resource.DNACTResource
import com.greencross.lims.report.ON206.template.DNACTTemplate
import java.awt.Color

class SectionClinicalInformation(private val y: Float = 625f): Painter<DNACTTemplate<DNACTResource>, DNACTDto> {
    override fun paint(
        stream: PDPageContentStreamPageAccessible,
        template: DNACTTemplate<DNACTResource>,
        dto: DNACTDto
    ): PDPageContentStreamPageAccessible {
        stream.saveGraphicsState()
        stream.paragraph(35f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentBold().clone().color(Color(0,80,109)).fontSize(15f), "臨床情報"))
        stream.paragraph(112f, y-14f, 400f, AlignHorizontal.LEFT, TextBlock(template.resource().styleContentRegualar().clone().color(Color(0,0,0)).fontSize(15f), toJPCancerName(dto.result[0].cancer)))

        stream.setLineWidth(0.8f).setStrokingColor(Color(0,0,0)).setNonStrokingColor(Color(0,0,0)).moveTo(35f, y-28f).lineTo(560f,y-28f).fill()
        stream.restoreGraphicsState()
        return stream
    }
    private fun toJPCancerName(cancer: String) = when(cancer) {
        "Liver cancer"                          -> "肝がん"
        "Colorectal cancer"                     -> "大腸がん"
        "Lung cancer"                           -> "肺がん"
        "Esophageal cancer"                     -> "食道がん"
        "Pancreatic cancer"                     -> "膵臓がん"
        "Ovarian cancer"                        -> "卵巣がん"
        "Thyroid cancer"                        -> "甲状腺がん"
        "Testicular cancer"                     -> "精巣がん"
        "Myelodysplastic syndrome"              -> "骨髄異形成症候群"
        "Bone tumor"                            -> "骨腫瘍"
        "Glioblastoma"                          -> "膠芽腫"
        "Oral cancer"                           -> "口腔がん"
        "Oropharyngeal cancer"                  -> "下咽頭がん"
        "Acute myeloid leukemia"                -> "急性骨髄性白血病"
        "Acute lymphocytic leukemia"            -> "急性リンパ性白血病"
        "Brain tumors"                          -> "脳腫瘍"
        "Pituitary adenoma"                     -> "下垂体腺腫"
        "Multiple myeloma"                      -> "多発性骨髄腫"
        "Biliary tract cancer"                  -> "胆道がん"
        "Gallbladder cancer"                    -> "胆嚢がん"
        "Lymphoma"                              -> "リンパ腫"
        "Chronic myeloid leukemia"              -> "慢性骨髄性白血病"
        "Chronic lymphocytic leukemia"          -> "慢性リンパ性白血病"
        "Retinoblastoma"                        -> "網膜芽細胞腫"
        "Diffuse large B-cell lymphoma"         -> "びまん性大細胞型B細胞リンパ腫（DLBCL）"
        "Ampullary cancer"                      -> "ファーター乳頭部がん"
        "Bladder cancer"                        -> "膀胱がん"
        "Nasal tumors"                          -> "鼻腔腫瘍"
        "Small intestine cancer"                -> "小腸がん"
        "Meningioma"                            -> "髄膜腫"
        "Kidney cancer"                         -> "腎臓がん"
        "Glioma"                                -> "神経膠腫"
        "Neuroblastoma"                         -> "神経芽細胞腫"
        "Malignant mesothelioma"                -> "悪性中皮腫"
        "Eye cancer"                            -> "眼がん"
        "Ureteral cancer"                       -> "尿管がん"
        "Urethral cancer"                       -> "尿道がん"
        "Gastric cancer"                        -> "胃がん"
        "Gastrointestinal tract Stromal tumor"  -> "消化管間質腫瘍（GIST）"
        "Cancer of unknown primary site"        -> "原発不明がん"
        "Breast cancer"                         -> "乳がん"
        "Sarcoma"                               -> "肉腫"
        "Cervical cancer"                       -> "子宮頸がん"
        "Endometrial cancer"                    -> "子宮体がん（子宮内膜がん）"
        "Prostate cancer"                       -> "前立腺がん"
        "Rectal cancer"                         -> "直腸がん"
        "Spine tumors"                          -> "脊椎腫瘍"
        "Vestibular schwannoma"                 -> "聴神経鞘腫"
        "Salivary gland cancer"                 -> "唾液腺がん"
        "Skin cancer"                           -> "皮膚がん"
        "Rhabdomyosarcoma"                      -> "横紋筋肉腫"
        "Laryngeal cancer"                      -> "喉頭がん"
        "Thymic cancer"                         -> "胸腺がん"
        "Melanoma"                              -> "悪性黒色腫（メラノーマ）"

        else -> throw Exception("등록되지 않은 암종입니다.")
    }
}
