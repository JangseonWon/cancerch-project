package com.greencross.lims.report.avoid.kokr

import com.gcgenome.lims.avoid.TestInfo
import com.greencross.lims.report.avoid.AvoidDto
import com.greencross.lims.report.avoid.AvoidTemplateN201
import com.greencross.lims.report.builder.Sex

class AvoidTemplateN201KoKr(
    resource: AvoidResourceN201KoKr,
    testInfo: TestInfo
) : AvoidTemplateKoKr<AvoidResourceN201KoKr>(testInfo),
    AvoidTemplateN201<AvoidResourceN201KoKr> {
    private val resource: AvoidResourceN201KoKr = resource

    override fun resource(): AvoidResourceN201KoKr {
        return resource
    }

    override fun lblTitleSmallLogo() = "[Pan-cancer : 주요 암]"
    override fun lblMedicalInstitution () = "의뢰기관"
    override fun lblRequestNumber      () = "접수번호"
    override fun lblPatientName        () = "성명"
    override fun lblAgeSex             () = "나이/성별"
    override fun lblMedicalRecordNumber() = "등록번호"
    override fun lblSpecimenType       () = "검체종류"
    override fun lblSpecimenDate       () = "검체채취일"
    override fun lblReceiptReportDate  () = "접수일/보고일"
    override fun lblIntroHeader() = "인공지능 액체생검 주요 6종 암 선별검사"
    override fun lblIntroContent() = "AVOID 검사는 약 1,600명의 암 환자 및 정상인에서 특징적으로 나타나는 DNA 패턴을 " +
            "학습한 인공지능으로 수검자의 DNA 패턴을\n분석하여 주요 6종 암의 존재 가능성을 예측합니다. " +
            "본 검사의 결과는 암의 진단 혹은 완전한 배제를 의미하지 않습니다."

    override fun lblOverviewTitle() = "종 합 결 과"
    override fun lblOverviewCommon() = "인공지능 알고리즘을 통해 DNA를 분석한 결과,\n"
    override fun lblOverviewBridgeWord() = "님은 "
    override fun lblOverviewLowLisk() = " 대상자 입니다.\n현재 암 존재 가능성이 낮게 예측됩니다.\n" +
            "그러나 암에 걸리지 않는다는 것을 의미하지 않으므로, \n" +
            "국가 암 검진 권고사항에 따라 정기적인 건강검진을 권장합니다."

    override fun lblOverviewRiskBridge() = " 대상자 입니다.\n 암환자군과 다소 유사한 "
    override fun lblOverViewRisk() = "이상 패턴이 발견"
    override fun lblOverViewMiddleRisk() = "되었으나,\n정확한 암종에 대한 구분은 어렵습니다.\n"
    override fun lblOverViewHighRisk() = "되었으며,\n6종 암 중 가장 의심되는 암종은 "
    override fun lblOverViewHighRiskEnd() = "입니다.\n"
    override fun lblOverviewMidHighEnd(risk: AvoidDto.Results) : String {
        val case = when(risk){
            AvoidDto.Results.CONCERN -> "약 5%는 관심관리"
            else                     -> "약 1%는 집중관리"
        }
        return "\n본 검사는 암의 존재 가능성을 예측하는 검사로\n" +
            "정상인이라도 건강상태에 따라 ${case}로 보고될 수 있습니다.\n" +
            "확진을 위해서는 의료인과의 상담을 통한 정밀검사를 권장합니다."
    }

    override fun lblDoubtSquareTitle() = "이상 패턴 검출 여부"
    override fun lblDoubtSquareContent(result: AvoidDto.Results) = when (result) {
        AvoidDto.Results.GENERAL -> "미검출"
        else -> "검 출"
    }

    override fun lblDoubtContentTitle() = "6종 암 중 인공지능 예측 암종"
    override fun lblDoubtContentLarge(result: AvoidDto.Results, name: String) = when (result) {
        AvoidDto.Results.CONCERN -> "해당없음 : 추적관찰 권장"
        AvoidDto.Results.RISK -> "6종 암 중 $name" + "의 DNA 패턴과 가장 유사합니다."
        else -> "해당없음"
    }

    override fun lblDoubtContentSmall(result: AvoidDto.Results) = when (result) {
        AvoidDto.Results.GENERAL -> "DNA 패턴 분석 결과, 암 존재 가능성이 낮게 예측되었습니다."
        AvoidDto.Results.RISK-> "본 검사에 포함된 6종 암 중 가장 유사한 암종에 대해 예측하므로,\n" +
                "타 암종에 대해서는 정확한 분석이 어렵습니다."
        else -> "DNA 패턴 분석 결과, 6종 암으로 예측되지는 않습니다.\n " +
                "그러나 암의 존재 가능성이 발견되었으므로 추적관찰을 권장합니다."
    }

    override fun lblDangerTitle() = "암종별 위험도"
    override fun lblDangerIntroStart() = "암종별 위험도는 수검자가 "
    override fun lblDangerIntroBridge() = "실제로 암일 확률"
    override fun lblDangerIntroEnd() = "을 동일집단(동일한 연령대, 성별)과 비교합니다."
    override fun lblDangerCancerName(code: Int) = when(code){
        1 -> "폐암"
        2 -> "대장암"
        3 -> "간암"
        4 -> "췌장담도암"
        5 -> "식도암"
        6 -> "난소암"
        7 -> "기타 암종"
        8 -> "유방암"
        else -> ""
    }
    override fun lblDangerGraphGuide() = "평균 위험도    수검자"
    override fun lblDangerTMI() = "* 각 암종별 평균 위험도는 수검자와 동일한 연령대, 성별에서의 유병률에 해당합니다." +
            "(국가암등록사업 연례 보고서-2018 암등록통계)"

    override fun lblDetailResultAnalysisHeader(name: String) = name + "님의 상세 결과 해석"
    override fun lblDetailResultAnalysis(patient: String) = "[ $patient ]님의\n인공지능 알고리즘을 통한\nDNA 분석 결과는"
    override fun lblDetailResultAnalysisTableHeaderTop() = "암종"
    override fun lblDetailResultAnalysisTableHedaerBot() = "위험도 비교"
    override fun lblDetailResultAnalysisTableNone1() = "해당없음"
    override fun lblDetailResultAnalysisTableNone2() = "암의 존재가능성 낮음"

    override fun lblDetailResultAnalysisTableMidRisk1() = "해당없음 : 추적관찰 권장"
    override fun lblDetailResultAnalysisTableMidRisk2() = "특정 암으로 예측하기에는\n불분명하나 암 존재 가능성이\n약 2배 이상 높을 것으로 예측됨"
    override fun lblDetailResultAnlaysisTableContentCom() = "약 1배"
    override fun lblDetailResultAnalysisTableContentMID() = "약 2배 이상"
    override fun lblDetailResultAnalysisTableContentHIG1() = "약 5배 이상"
    override fun lblDetailResultAnalysisTableContentHIG2() = "약 10배 이상"
    override fun lblDetailResultAnalysisContentLine1() = "혈액 속 암세포에서 유래된 DNA를 인공지능 알고리즘을 통해 분석한 결과,\n"
    override fun lblDetailResultAnalysisContentLine2NRM() = "님은 암의 존재 가능성이 낮을 것으로 예측되어 "
    override fun lblDetailResultAnalysisContentLine2MID() = "님은 암의 존재 가능성이 다소 높을 것으로 예측되어 "
    override fun lblDetailResultAnalysisContentLine2HIG() = "님은 암의 존재 가능성이 높을 것으로 예측되어 "
    override fun lblDetailResultAnalysisContentLine2END() = " 대상자 입니다.\n\n"
    override fun lblDetailResultAnalysisContentLine3NRM() =
        "AVOID 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성능이 달라질 수 있습니다.\n" +
                "본 검사는 수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아닙니다.\n\n" +
                "현재 암 일반관리군이더라도 암에 걸리지 않는 것은 아니므로,\n" +
                "정기적인 건강검진과 생활습관 관리를 통해 암을 예방할 것을 권장합니다."

    override fun lblDetailResultAnalysisContentLine3MID_1() = "님의 암세포 유래 DNA 이상 패턴은 암환자와 다소 유사하여\n" +
            "암 존재 가능성이 "

    override fun lblDetailResultAnalysisContentLine3MID_2() = " 높을 것으로 예측되나, 암종을 예측하기에는 불분명합니다.\n" +
            "관심관리 대상자여도 암이 아닐 수 있으며, 암으로 확진되기까지 수 개월이 걸릴 수도 있습니다.\n\n" +
            "정상인 대비 DNA 패턴이 암환자와 유사하지만 추적 관찰을 통한 확인이 필요한 경우 "

    override fun lblDetailResultAnalysisContentLine3MID_3() = " 대상자로 보고됩니다.\n 하지만"
    override fun lblDetailResultAnalysisContentLine3MID_4() =
        " 정상인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 관심관리 대상자로 보고될 수 있습니다(약 5%).\n"

    override fun lblDetailResultAnalysisContentLine3HIG_1() =
        "님의 암세포 유래 DNA 이상 패턴은 주요 6종 암(대장암, 폐암, 간암, 췌장담도암, 식도암, 난소암) 중\n"

    override fun lblDetailResultAnalysisContentLine3HIG_2() = " 환자의 DNA 이상 패턴과 가장 유사합니다.\n일반적으로 "
    override fun lblDetailResultAnalysisContentLine3HIG_3() = " 환자는 "
    override fun lblDetailResultAnalysisContentLine3HIG_4() = "(10만명 중에 "
    override fun lblDetailResultAnalysisContentLine3ASR(asr: String) = "${asr}명"
    override fun lblDetailResultAnalysisContentLine3HIG_5() = ")의 확률로 발생하지만,\nAVOID 검사 결과 "
    override fun lblDetailResultAnalysisContentLine3HIG_6() = "군인 "
    override fun lblDetailResultAnalysisContentLine3HIG_7() = "님은 "
    override fun lblDetailResultAnalysisContentLine3HIG_8() = "일 확률이 "
    override fun lblDetailResultAnalysisContentLine3HIG_9() = "로\n일반인 대비 "
    override fun lblDetailResultAnalysisContentLine3HIG_10() = " 존재 가능성이 약 10배 이상 높을 것으로 예측됩니다.\n" +
            "집중관리 대상자여도 암이 아닐 수 있으며, 암으로 확진되기까지 수 개월이 걸릴 수도 있습니다.\n\n" +
            "정상인 대비 DNA 패턴이 암환자와 유사한 경우 "

    override fun lblDetailResultAnalysisContentLine3HIG_11() = " 대상자로 분류됩니다.\n하지만"
    override fun lblDetailResultAnalysisContentLine3HIG_12() =
        " 정상인이라도 건강상태(양성질환, 자가면역질환 등)에 따라 집중관리 대상자로 보고될 수 있습니다(약 1%).\n"

    override fun lblDetailResultAnalysisContentLine3MIDHIG() = "본 검사는 수검자의 암 존재 가능성을 확인하는 검사로 정확한 진단을 위한 검사는 아니며,\n" +
            "확진을 위해서는 의료진 상담을 통한 정밀 검사를 권장합니다."

    override fun lblGuideLineHeader(type: AvoidDto.Results, name: String) = when (type) {
        AvoidDto.Results.GENERAL -> "암 검진 가이드라인"
        else -> name + "님의 맞춤 가이드라인"
    }
    override fun lblGuideLineTableHeader1() = "정밀검사"
    override fun lblGuideLineTableHeader2() = "AVOID 검사 모니터링 권장 기간"
    override fun lblGuideLineTop(type: AvoidDto.Results, cancer: String) = when (type) {
        AvoidDto.Results.CONCERN -> "관심관리군 맞춤 가이드라인은 다음과 같습니다"
        else -> "$cancer 집중관리군 맞춤 가이드라인은 다음과 같습니다."
    }
    override fun lblGuideLineDetection(): String {
        return "· 주치의와 상담요함"
    }

    override fun lblGuideLineTime(cancer: String): String {
        return when (cancer) {
            "폐암" -> "3개월 후"
            "대장암" -> "3개월 후"
            "간암" -> "3개월 후"
            "췌장암" -> "3개월 후"
            "식도암" -> "3개월 후"
            "유방암" -> "3개월 후"
            "난소암" -> "3개월 후"
            else -> "3개월 후"
        }
    }

    override fun lblGuideLineNormalHeader(code: Int) = when(code) {
        0->"암종"
        1->"대상"
        2->"주기"
        else->"검사"
    }

    override fun lblGuideLineNormalCancer(code: Int) = when(code) {
        0->"폐암"
        1->"대장암"
        2->"간암"
        3->"췌장담도암"
        4->"식도암"
        else -> "난소암"
    }

    override fun lblGuideLineNormalTarget(code: Float) = when(code) {
        0f->"만 54세 이상 만 74세 이하 남녀\n 폐암 발생 고위험군(30갑년*이상 흡연력)"
        1f->"만 50세 이상 남녀"
        2f->"만 40세 이상 남녀 중 간암 발생 고위험군\n"
        2.5f->"(간경변증이나 B형 간염 바이러스 항원 또는\nC형 간염 바이러스 항체 양성으로 확인된 자)"
        3f->"만 70세 이상 남녀\n췌장담도암 가족력/장기 흡연자/만성췌장염 병력"
        4f->"증상이 있거나, 식도암이 의심되는 자"
        else -> "-"
    }

    override fun lblGuideLineNormalFrequency(code: Int) = when(code) {
        0->"2년"
        1->"1년"
        2->"6개월"
        3->"정기적인\n검사 권장"
        else -> "-"
    }

    override fun lblGuideLineNormalTest(code: Float) = when(code){
        0f->"저선량흉부CT검사"
        1f->"분변잠혈검사 이상소견 시, 대장내시경검사\n"
        1.5f->"(단, 대장내시경을 실시하기 어려운 경우 대장이중조영검사 선택적 시행)"
        2f->"간초음파검사, 혈청알파태아단백검사"
        3f->"복부초음파검사, 복부CT검사"
        4f->"식도-위 내시경검사"
        else -> "CA125 암표지자 검사 이상소견 시 초음파검사, CT검사, MRI검사"
    }

    override fun lblGuideLineCaption() = "*갑년 : 일평균 흡연량(갑) x 흡연기간(년) ex) 2갑 x 15년 = 30갑년(검진 대상)"

    override fun lblGuideLineComment(cancer: String): String {
        return when {
            cancer == "폐암" -> "AVOID 검사 폐암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 폐암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "대장암" -> "AVOID 검사 대장암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "시행이 어려운 경우 대장이중조영검사를 통해서도 대장암 여부를 확인할 수 있습니다.\n" +
                    "정밀검사를 통해 대장암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "간암" -> "AVOID 검사 간암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 간암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "췌장암" -> "AVOID 검사 췌장담도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 췌장담도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "식도암" -> "AVOID 검사 식도암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 식도암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "유방암" -> "AVOID 검사 유방암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 유방암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            cancer == "난소암" -> "AVOID 검사 난소암 집중관리군은 정밀검사를 위해 주치의와 상담을 권장합니다.\n" +
                    "정밀검사를 통해 난소암이 확인 되지 않은 경우, 다른 암종의 가능성을 완전히 배제할 수 없습니다.\n" +
                    "증상 등이 동반되어 다른 암종이 의심될 경우 PET-CT 검사를 고려할 수 있습니다.\n" +
                    "정밀 검사에서 암이 확인되지 않았다면 3개월 주기로 본 검사를 통해 암 DNA를 추적할 것을 권장합니다."
            else -> "AVOID 검사 관심관리군은 3개월 후 본 검사를 통해 암 DNA를 추적할 것을 권장합니다.\n" +
                    "관심관리군은 암환자와 다소 유사한 DNA 이상 패턴이 관찰되었으나,\n" +
                    "건강상태(양성질환, 자가면역질환 등)에 따른 일시적인 이상 패턴 검출의 가능성을 배재할 수 없는 경우입니다.\n" +
                    "3개월 주기로 본 검사를 통해 암 DNA에 의한 이상 패턴을 추적할 것을 권장합니다.\n" +
                    "증상 등이 동반되어 특정 암종이 의심될 경우 의료진 상담을 통한 해당 암종에 대한 정밀 검사를 권장합니다."
        }
    }

    override fun lblDetailProcessHeader() = "검 사 상 세 설 명"
    override fun lblDetailProcessTitle()  = "[ AVOID 검사 과정 ]"
    override fun lblDetailProcessContentCode(code: Int) = when(code){
        0 -> "수검자"
        1 -> "유전체 정보 생산\n"
        2 -> "; 차세대염기서열분석검사(NGS)"
        3 -> "혈액"
        4 -> "입력"
        5 -> "암 존재 가능성 확인"
        6 -> "정상인"
        7 -> "간암"
        8 -> "췌장담도암"
        9 -> "대장암"
        10 -> "학습된 알고리즘"
        11 -> "액체생검(Liquid Biopsy; LBx)"
        12 -> "차세대염기서열분석검사\n(Next-Generation Sequencing; NGS)"
        13 -> "인공지능(Artificial Intelligence; AI) - 특허출원"
        14 -> "인공지능 기술 중 하나인 딥 러닝(deep learning) 기술을 이용해\n" +
                "수검자의 암세포에서 유래된 cfDNA를 분석합니다.\n" +
                "딥 러닝은 경험적 데이터를 기반으로 학습하여, 새로운 데이터를 예측하고 스스로의\n" +
                "성능을 향상시키는 인공지능(AI) 기술입니다.\n" +
                "GC지놈에서 자체개발한 AVOID 검사의 딥 러닝 알고리즘은"
        15 -> " 약 1천명 이상의\n" +
                "암환자와 정상인의 cfDNA의 특징을 학습"
        16 -> "하였습니다. 이를 바탕으로 수검자의 cfDNA를\n" +
                "분석하여 암의 존재 가능성을 예측할 수 있습니다."
        17 -> "Artificial Intelligence"
        18 -> "Machine Learning"
        else -> "Deep Learning"
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

    override fun lblLimitationHeader() = "검 사 한 계"
    override fun lblLimitationTableHeader(col: Int) = when(col){
        0       -> "암종"
        1       -> "특이도¹⁾"
        2       -> "민감도²⁾"
        3       -> "양성예측도³⁾"
        else    -> "음성예측도⁴⁾"
    }
    override fun lblLimitationTableTotalCancer(col: Int) = when(col){
        0       -> "전체"
        1       -> "95.0%"
        2       -> ">90.0%"
        3       -> "65.9%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTableLungCancer(col: Int) = when(col){
        0       -> "폐암"
        1       -> "95.0%"
        2       -> ">90.0%"
        3       -> "9.4%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTableColorCancer(col: Int) = when(col){
        0       -> "대장암"
        1       -> "95.0%"
        2       -> "87.3%"
        3       -> "14.0%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTableLiverCancer(col: Int) = when(col){
        0       -> "간암"
        1       -> "95.0%"
        2       -> ">90.0%"
        3       -> "6.4%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTablePanCancer(col: Int) = when(col){
        0       -> "췌장담도암"
        1       -> "95.0%"
        2       -> ">90.0%"
        3       -> "1.4%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTableEsopCancer(col: Int) = when(col){
        0       -> "식도암"
        1       -> "95.0%"
        2       -> ">90.0%"
        3       -> "1.2%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTableOverCancer(col: Int) = when(col){
        0       -> "난소암"
        1       -> "95.0%"
        2       -> "89.8%"
        3       -> "1.3%"
        else    -> ">98.0%"
    }

    override fun lblLimitationTable2Header(col: Int) = when(col){
        0       -> "암종"
        1       -> "검사"
        2       -> "특이도"
        3       -> "민감도"
        4       -> "양성예측도"
        else    -> "음성예측도"
    }
    override fun lblLimitationTable2Row1(col: Int) = when(col){
        0       -> "폐암"
        1       -> "저선량흉부CT검사"
        2       -> "약 92.6%"
        3       -> "약 88.9%"
        4       -> "약 6.3%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row2(col: Int) = when(col){
        0       -> "대장암"
        1       -> "분변잠혈검사"
        2       -> "95.4%"
        3       -> "약 40.0%"
        4       -> "약 7.5%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row3(col: Int) = when(col){
        0       -> "대장내시경 검사"
        1       -> "약 99.0%"
        2       -> "약 85.0~95.0%"
        3       -> "약 44.1~46.9%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row4(col: Int) = when(col){
        0       -> "간암"
        1       -> "간초음파 및 혈청알파태아단백검사"
        2       -> "약 94.0%"
        3       -> "약 80.0%"
        4       -> "약 4.5%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row5(col: Int) = when(col){
        0       -> "췌장담도암"
        1       -> "CA19-9 암표지자 검사"
        2       -> "약 79.9~85.3%"
        3       -> "약 76.1~80.2%"
        4       -> "약 0.3~0.4%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row6(col: Int) = when(col){
        0       -> "식도암"
        1       -> "식도-위내시경 검사"
        2       -> "약 79.0%"
        3       -> "약 62.0%"
        4       -> "약 0.2%"
        else    -> ">98%"
    }
    override fun lblLimitationTable2Row7(col: Int) = when(col){
        0       -> "난소암"
        1       -> "CA125 암표지자 검사"
        2       -> "약 95.0%"
        3       -> "약 43.3%"
        4       -> "약 0.6%"
        else    -> ">98%"
    }

    override fun lblLimitation(row: Int): String {
        return when (row) {
            0 -> "본 검사는 암세포 유래 cfDNA 특성 분석을 통해 암의 존재 가능성을 예측하는 검사로, 확진 목적으로 사용할 수 없습니다."
            1 -> "본 검사는 모든 암을 검출할 수 없으며, 암의 병기나 종류에 따라 검출 성적이 달라질 수 있습니다."
            2 -> "본 검사의 데이터는 주요 암종인 폐암, 대장암, 간암, 췌장담도암, 식도암, 난소암을 포함하고 있으며 기타 암종은 정확한 분석이 어렵습니다."
            3 -> "암종의 위치 및 유전적 특성에 따라 검출민감도가 상이할 수 있습니다."
            4 -> "본 검사는 내부적으로 축적된 데이터에 따라 검사 대상 암종 확대 및 성능이 변경될 수 있습니다."
            5 -> "본 검사는 양성질환, 자가면역질환 등에서 위양성으로 보고될 수 있으며, 항암치료, 세포치료 등에 따라서 위음성으로 보고될 수 있습니다."
            else -> ""
        }
    }

    override fun lblLimitationDescription(row: Int): String {
        return when (row) {
            0 -> "1) 특이도 : 정상인을 검사했을 때 AVOID Pan-cancer Screen 검사가 일반관리군으로 판단한 비율을 의미합니다."
            1 -> "2) 민감도 : 암환자를 검사했을 때 AVOID Pan-cancer Screen 검사가 관심관리군 · 집중관리군으로 판단한 비율을 의미합니다."
            2 -> "3) 양성예측도 : AVOID Pan-cancer Screen 검사에서 관심관리 · 집중관리로 판단한 수검자가 실제 암환자일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 양성예측도가 계산되었습니다."
            3 -> "4) 음성예측도 : AVOID Pan-cancer Screen 검사에서 일반관리로 판단한 수검자가 실제 정상인일 비율을 의미합니다. 50대 이상의 유병률에 기초하여 음성예측도가 계산되었습니다."
            else -> ""
        }
    }

    override fun lblReferenceTitle() = "참 고 문 헌"

    override fun lblReferenceLeft(): String {
        return  "1.\tCancer Biol Ther. 2019; 20(8): 1057–1067.\n" +
                "2.\tMutat Res. Jul-Sep 2019;781:100-129.\n" +
                "3.\tBMC Cancer. 2017; 17: 697.\n" +
                "4.\tCancer Resaerch. 2022:82(12, Supplement):6371-6371\n"+
                "5.\tBr J Cancer. 2008;98(10):1602-7.\n"+
                "6.\t대한소화기내시경학회지, 2007;35(2): 68-73."
    }

    override fun lblReferenceRight(): String {
        return  "7.\tJAMA . 2016;315(23):2564-2575.\n" +
                "8.\tAliment Pharmacol Ther. 2009;30(1):37-47.\n"+
                "9.\tOnco Targets Ther. 2016;9:7459-7467.\n" +
                "10.\tCurr Mol Med. 2013;13(3):340-51.\n" +
                "11.\tWorld J Gastroenterol. 2015;21(26):7933-43.\n" +
                "12.\tGynecol Oncol. 2008;108(2):402-8."

    }

    override fun lblReferenceDescription(col: Int): String {
        return when {
            col == 0 -> "※ 본 검사는 검사 결과가 갖는 임상적 의미가 확립되지 않았으며, 이에 따르는 건강에 관련된 행위가 유용하다는 객관적 타당성이 아직 부족합니다.\n" +
                    "※ 이 검사는 "
            col == 1 -> "하였습니다."
            else -> ""
        }
    }

    override fun lblReferenceDescriptionBold(): String {
        return "GC지놈에서 자체 개발한 검사(Laboratory-developed Test, LDT)로 적절한 평가를 통해 성능을 확인"
    }

    override fun lblPerformance(): String {
        return "AVOID 검사의 암종별 성능"
    }

    override fun lblPerformanceBasic(): String = "기존 선별검사 성능"

    override fun lblResultToWord(result: AvoidDto.Results) = when(result) {
        AvoidDto.Results.GENERAL -> "일반관리"
        AvoidDto.Results.CONCERN -> "관심관리"
        else -> "집중관리"
    }

    override fun lblPatientInfo(age: String, sex: Sex) : String {
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
    override fun lblPatientSir(name: String,) = "${name}님"
    override fun lblCancerToWord(cancer: String) = when(cancer){
        "췌장암" -> "췌장담도암"
        "기타암종" -> "기타 암"
        else -> cancer
    }

    override fun lblDetailResultAnalysisTableConcent(cancer: String) = "의심 암종 : ${lblCancerToWord(cancer)}"
    override fun lblDetailResultAnalysisTableConcent(): String = "기타 암"
}