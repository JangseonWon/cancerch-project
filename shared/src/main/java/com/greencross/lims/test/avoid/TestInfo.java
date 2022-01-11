package com.greencross.lims.test.avoid;

import com.greencross.lims.test.HasCode;
import com.greencross.lims.test.HasGenes;
import com.greencross.lims.test.MayBeNationalInsurance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;

@Data
@Accessors(fluent = true)
@Builder
public class TestInfo implements HasCode, HasGenes, MayBeNationalInsurance {
    @Data
    @Accessors(fluent = true)
    @Builder
    public static class Gene {
        private String gene;
        private String code;
    }
    private final String code;
    private final String name;
    private final com.greencross.lims.test.avoid.TestInfo.Gene[] cores;
    @Override
    public String[] genes() {
        LinkedList<String> genes = new LinkedList<>();
        if(cores!=null) for(Gene gene: cores) genes.add(gene.gene());
        return genes.toArray(new String[0]);
    }
    @Builder.Default
    private final String summaryCode = null;
    private final String interpretationCode;
    @Builder.Default
    private final boolean isNationalInsuranceTest = false;
    public static final TestInfo N201 = TestInfo.builder()
            .code("N201")
            .interpretationCode("N075440")
            .cores(new com.greencross.lims.test.avoid.TestInfo.Gene[]{
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075010").gene("COL3A1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075020").gene("FBN1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075030").gene("TGFBR1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075040").gene("SMAD3").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075050").gene("TGFBR2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075060").gene("ACTA2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075070").gene("MYH11").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075120").gene("MYBPC3").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075130").gene("MYH7").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075150").gene("TNNI3").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075160").gene("TPM1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075170").gene("MYL3").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075180").gene("ACTC1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075190").gene("PRKAG2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075200").gene("MYL2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075210").gene("LMNA").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075220").gene("GLA").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075080").gene("RYR2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075090").gene("KCNQ1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075100").gene("KCNH2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075110").gene("SCN5A").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075230").gene("PKP2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075240").gene("DSP").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075250").gene("DSC2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075260").gene("TMEM43").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075270").gene("DSG2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075280").gene("LDLR").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075290").gene("APOB").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075300").gene("PCSK9").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075310").gene("PROC").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075320").gene("PROS1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075330").gene("약물 SLC47A2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075340").gene("약물 CYP2C9").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075350").gene("약물 TCF7L2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075360").gene("약물 COQ2").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075370").gene("약물 CACNA1C").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075380").gene("약물 AGTR1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075390").gene("약물 ADRB1").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075400").gene("약물 ACE").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075410").gene("약물 NEDD4L").build(),
                    com.greencross.lims.test.avoid.TestInfo.Gene.builder().code("N075420").gene("약물 CYP2C19").build()
            }).build();

    @Data
    @Accessors(fluent = true)
    @Builder
    public static class Genotype {
        private String gene;
        private String pos;
        private String[] types;
    }
}
