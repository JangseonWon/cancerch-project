package com.gcgenome.lims.avoid;

import com.gcgenome.lims.test.HasCode;
import com.gcgenome.lims.test.HasGenes;
import com.gcgenome.lims.test.MayBeNationalInsurance;
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
    private final TestInfo.Gene[] cores;
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
            .code("N201").build();
    @Data
    @Accessors(fluent = true)
    @Builder
    public static class Genotype {
        private String gene;
        private String pos;
        private String[] types;
    }

    public static final TestInfo[] TESTS = new TestInfo[] {
        N201
    };
}
