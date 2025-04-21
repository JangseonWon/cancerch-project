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
            .code("N201").name("AVOID").build();

    public static final TestInfo N203 = TestInfo.builder()
            .code("N203").name("아이캔서치").build();

    public static final TestInfo N204 = TestInfo.builder()
            .code("N204").name("아이캔서치(연구용)").build();

    public static final TestInfo N205 = TestInfo.builder()
            .code("N205").name("아이캔서치(연구용)").build();

    public static final TestInfo N206 = TestInfo.builder()
            .code("N206").name("아이캔서치(연구용)").build();

    public static final TestInfo ON203 = TestInfo.builder()
            .code("ON203").name("아이캔서치(영문)").build();

    public static final TestInfo N256 = TestInfo.builder()
            .code("N256").name("아이캔서치(강북삼성용)").build();

    public static final TestInfo ON256 = TestInfo.builder()
            .code("ON256").name("아이캔서치(강북삼성용_영문)").build();

    public static final TestInfo ON204 = TestInfo.builder()
            .code("ON204").name("아이캔서치(일문)").build();

    public static final TestInfo ON206 = TestInfo.builder()
            .code("ON206").name("DNA-CT").build();

    public static final TestInfo J001 = TestInfo.builder()
            .code("J001").name("아이캔서치(강북삼성 1차 재검용)").build();

    public static final TestInfo J002 = TestInfo.builder()
            .code("J002").name("아이캔서치(강북삼성 2차 재검용)").build();

    public static final TestInfo J024 = TestInfo.builder()
            .code("J024").name("아이캔서치(아이메드)").build();

    public static final TestInfo[] TESTS = new TestInfo[] {
            N201,               //AVOID
            N203, N204, N205,   //Cancerch국문
            N206,               //아이캔서치 임직원
            ON203,              //Cancerch영문
            N256, ON256,     // 강북삼성 Cancerch 국/영문
            ON204, ON206     // 림포텍 아이캔서치
    };
}
