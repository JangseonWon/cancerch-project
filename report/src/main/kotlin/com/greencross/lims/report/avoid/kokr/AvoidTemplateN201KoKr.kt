package com.greencross.lims.report.avoid.kokr

import com.greencross.lims.report.avoid.AvoidTemplateN201
import com.greencross.lims.test.avoid.TestInfo

class AvoidTemplateN201KoKr(
    resource: AvoidResourceN201KoKr,
    testInfo: TestInfo
) : AvoidTemplateKoKr<AvoidResourceN201KoKr>(testInfo),
    AvoidTemplateN201<AvoidResourceN201KoKr> {
    private val resource: AvoidResourceN201KoKr = resource
    val lblIntroHeader           = "인공지능 액체생검 주요 암 선별검사"
    val lblIntroContent          = "AVOID 검사는 2,000명의 암 환자에서 특징적으로 나타나는 DNA이상 패턴을 " +
            "학습한 인공지능으로 수검자의 DNA 패턴을 분석하여 \n 주요 암의 존재 가능성을 예측합니다. " +
            "본 검사의 결과는 암의 진단 혹은 완전한 배제를 의미하지 않습니다."
    val lblOverviewTitle         = "종 합 결 과"
    val lblOverviewCommon        = "인공지능 알고리즘을 통해 DNA를 분석한 결과,\n"
    val lblOverviewLowRisk       = "입니다.\n현재 암 존재 가능성이 낮게 예측되어도 국가 암 검진\n" +
            "권고사항에 따라 정기적인 건강검진을 권장합니다."
    val lblOverviewHighLisk      = "입니다.\n본 검사는 암의 존재 가능성을 예측하는 검사로\n확진을 위해서는" +
            "의료인과의 상담을 통한 정밀검사를 권장합니다."
    val lblDoubtSquareTitle      = "이상 패턴"
    val lblDoubtContentTitle     = "의심 암종"
    val lblDangerTitle           = "암종별 위험도"
    val lblDangerIntro           = "암종별 위험도는 수검자와 동일 집단(동일한 연령대, 성별)이 " +
                             "실제로 암일 확률을 비교합니다."
    val lblDangerTMI             = "* 각 암종별 평균 위험도는 수검자와 동일한 연령대, 성별에서의 유병률에 해당합니다." +
            "(국가암등록사업 연례 보고서-2018 암등록통계)"

    override fun resource(): AvoidResourceN201KoKr { return resource    }

    override fun lblIntroHeader(): String       { return lblIntroHeader         }
    override fun lblIntroContent(): String      { return lblIntroContent        }
    override fun lblOverviewTitle(): String     { return lblOverviewTitle       }
    override fun lblOverviewCommon(): String    { return lblOverviewCommon      }
    override fun lblOverviewLowLisk(): String   { return lblOverviewLowRisk     }
    override fun lblOverviewHighLisk(): String  { return lblOverviewHighLisk    }
    override fun lblDoubtSquareTitle(): String  { return lblDoubtSquareTitle    }
    override fun lblDoubtContentTitle(): String { return lblDoubtContentTitle   }
    override fun lblDangerTitle(): String       { return lblDangerTitle         }
    override fun lblDangerIntro(): String       { return lblDangerIntro         }
    override fun lblDangerTMI(): String         { return lblDangerTMI           }
    override fun lblResultAnalysis(patient: String): String {
        return "[ $patient ]님의\n인공지능 알고리즘을 통한\nDNA 분석 결과는"
    }

    override fun lblGuideLineDetection(cancer: String): String {
        return when{
            cancer == "폐암" -> "· 저선량흉부CT검사\n· PET-CT"
            cancer == "대장암" -> "· 대장내시경검사\n· 대장이중조영검사\n· PET-CT"
            cancer == "간암" -> "· 간초음파검사\n· 혈청알파태아단백검사\n· PET-CT"
            cancer == "췌장암" -> "· 복부초음파검사\n· 복부CT검사\n· PET-CT"
            cancer == "식도암" -> "· 식도-위 내시경검사\n· 식도조영검사\n· PET-CT"
            cancer == "유방암" -> "· 유방촬영검사\n· PET-CT"
            cancer == "난소암" -> "· 초음파검사\n· CT검사\n· MRI검사\n· PET-CT"
            else -> "· PET-CT"
        }
    }

    override fun lblGuideLineTime(cancer: String): String {
        return when{
            cancer == "폐암"   -> "3개월 후"
            cancer == "대장암" -> "3개월 후"
            cancer == "간암"   -> "3개월 후"
            cancer == "췌장암" -> "3개월 후"
            cancer == "식도암" -> "3개월 후"
            cancer == "유방암" -> "3개월 후"
            cancer == "난소암" -> "3개월 후"
            else -> "3개월 후"
        }
    }

    override fun lblGuideLineComment(cancer: String): String {
        return when{
            cancer == "폐암"   -> "AVOID 검사 폐암 고위험군은 저선량흉부CT검사를 권장합니다.\n" +
                    "정밀검사를 통해 폐암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "대장암" -> "AVOID 검사 대장암 고위험군은 대장내시경검사를 권장합니다.\n" +
                    "시행이 어려운 경우 대장이중조영검사를 통해서도 대장암 여부를 확인할 수 있습니다.\n" +
                    "정밀검사를 통해 대장암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "간암"   -> "AVOID 검사 간암 고위험군은 간초음파검사와 혈청알파태아단백검사를 권장합니다.\n" +
                    "정밀검사를 통해 간암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "췌장암" -> "AVOID 검사 췌장암 고위험군은 복부초음파검사, CT검사, MRI검사를 권장합니다.\n" +
                    "정밀검사를 통해 췌장암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "식도암" -> "AVOID 검사 식도암 고위험군은 식도-위내시경검사 혹은 식도조영검사를 권장합니다.\n" +
                    "정밀검사를 통해 식도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "유방암" -> "AVOID 검사 유방암 고위험군은 유방촬영검사를 권장합니다.\n" +
                    "정밀검사를 통해 유방암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "난소암" -> "AVOID 검사 난소암 고위험군은 경질초음파검사, CT검사, MRI검사를 권장합니다.\n" +
                    "정밀검사를 통해 난소암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            else -> "AVOID 검사 기타 암 고위험군은 PET-CT검사를 통한 암의 여부 및 암종 확인을 권장합니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
        }
    }

    override fun lblLBx(): String {
        return "액체생검은 혈액과 같은 체액을 이용해 질환을\n" +
                "선별하는 방법입니다.\n\n" +
                "혈액 속에는 질환 선별에 이용될 수 있는\n" +
                "다양한 바이오 마커들이 포함되어 있습니다.\n" +
                "그 중에서도 세포 유리 DNA(cell free DNA;\n" +
                "cfDNA)는 세포에서 혈액으로 방출된 DNA\n" +
                "입니다. 암세포에서 유래된 cfDNA는 암세포가\n" +
                "특징적으로 가지는 유전 정보를 포함하고\n" +
                "있습니다.\n\n" +
                "AVOID 검사는 암세포 유래 cfDNA 분석을 통해\n" +
                "암의 존재 및 기원을 예측 할 수 있습니다."
    }

    override fun lblNGS(): String {
        return "수검자의 혈액에서 분리한 세포 유리 DNA를\n" +
                "차세대염기서열분석기로 분석하여 암의 존재 가능성을\n" +
                "확인합니다."
    }

    override fun lblLimitation(row: Int): String{
        return when{
            row == 0 -> "본 검사는 암세포 유래 cfDNA 특성 분석을 통해 암의 존재 가능성을 예측하는 검사로, 확진 목적으로 사용할 수 없습니다."
            row == 1 -> "본 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성적이 달라질 수 있습니다."
            row == 2 -> "본 검사의 데이터는 주요 암종인 폐암, 대장암, 간암, 췌장암, 식도암, 유방암, 난소암을 포함하고 있으며 기타 암종은 정확한 분석이 어렵습니다."
            row == 3 -> "암종의 위치 및 유전적 특성에 따라 검출민감도가 상이할 수 있습니다."
            row == 4 -> "본 검사는 내부적으로 축적된 데이터에 따라 검사 대상 암종 확대 및 성능이 변경될 수 있습니다."
            row == 5 -> "본 검사는 양성질환, 자가면역질환 등에서 위양성으로 보고될 수 있으며, 항암치료, 세포치료 등에 따라서 위음성으로 보고될 수 있습니다."
            else -> ""
        }
    }

    override fun lblLimitationDescription(row: Int): String {
        return when{
            row == 0 -> "1) 특이도 : 정상인을 검사했을 때 AVOID Pan-cancer Screen 검사가 저위험군으로 판단한 비율을 의미합니다."
            row == 1 -> "2) 민감도 : 암환자를 검사했을 때 AVOID Pan-cancer Screen 검사가 고위험군으로 판단한 비율을 의미합니다."
            row == 2 -> "3) 양성예측도 : AVOID Pan-cancer Screen 검사에서 고위험으로 판단한 수검자가 실제 암환자일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 양성예측도가 계산되었습니다."
            row == 3 -> "4) 음성예측도 : AVOID Pan-cancer Screen 검사에서 저위험으로 판단한 수검자가 실제 정상인일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 음성예측도가 계산되었습니다."
            else -> ""
        }
    }

    override fun lblReferenceLeft(): String {
        return "1. Cancer Biol Ther. 2019; 20(8): 1057–1067.\n" +
                "2. Mutat Res. Jul-Sep 2019;781:100-129."
    }

    override fun lblReferenceRight(): String {
        return "3. BMC Cancer. 2017; 17: 697.\n" +
                "4. Jin Mo, Ahn et al. \"Highly sensitive deep learning algorithm for multi-cancer\n" +
                "       detection using cf-WGS\". AACR-KCA"
    }

    override fun lblReferenceDescription(col: Int): String{
        return when{
            col == 0 ->"※ 본 검사는 검사 결과가 갖는 임상적 의미가 확립되지 않았으며, 이에 따르는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.\n" +
                    "※ 이 검사는 "
            col == 1 -> "하였습니다."
            else -> ""
        }
    }

    override fun lblReferenceDescriptionBold(): String {
        return "GC녹십자지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 성능을 확인"
    }
    override fun lblPerformance(): String {
        return "AVOID 검사의 암종별 · 병기별 성능"
    }
}