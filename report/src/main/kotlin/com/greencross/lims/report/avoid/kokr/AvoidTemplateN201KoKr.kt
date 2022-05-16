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
}