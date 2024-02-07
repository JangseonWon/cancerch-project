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
public class TestInfo {
    private final String code;
    private final String name;
    public static final TestInfo N201 = TestInfo.builder()
            .code("N201").name("").build();
    public static final TestInfo N203 = TestInfo.builder()
            .code("N203").name("").build();
    public static final TestInfo N204 = TestInfo.builder()
            .code("N203").name("").build();
    public static final TestInfo N205 = TestInfo.builder()
            .code("N203").name("").build();
    public static final TestInfo N206 = TestInfo.builder()
            .code("N206").name("").build();
    public static final TestInfo ON203 = TestInfo.builder()
            .code("ON203").name("").build();

    public static final TestInfo[] TESTS = new TestInfo[] {
        N201,               //AVOID
        N203, N204, N205,   //Cancerch국문
        N206,               //아이캔서치 임직원
        ON203               //Cancerch영문
    };
}
