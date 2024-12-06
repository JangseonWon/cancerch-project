import {Analysis, SavableAnalysis, SearchResult, SelectedAnalysis} from "../types/Types";
import {RawAxiosResponseHeaders} from "axios";

const convertPredicatesToQueryString = (predicates: any): string => {
    const { page, limit, sort_by, asc, filters } = predicates;

    const filterArray = Object.entries(filters)
        .filter(([_, value]) => value === true || value instanceof Date) // true인 값과 날짜만 포함
        .map(([key, value]) => ({
            key,
            value: value instanceof Date
                ? value.getTime().toString()
                : (value as string | number | boolean).toString(), // 타입 단언 추가
        }));

    return `page=${page}&limit=${limit}&sort_by=${sort_by}&asc=${asc}&filters=${encodeURIComponent(
        JSON.stringify(filterArray)
    )}`;
};

function apiResponseToSearchResult(header: RawAxiosResponseHeaders, body: Analysis[]): SearchResult {
    return {
        data: body,
        total_page: Number(header["x-total-count"] ?? 0),
        current_page: Number(header["x-total-page"] ?? 1),
    };
}

function convertRowDataToSavableAnalysis(rowData: SavableAnalysis): {
    result: string;
    too6_pred: string;
    service: string;
    too5_pred: string;
    batch: string;
    row: number;
    sample: string
} {
    return {
        sample: rowData.sampleId.replaceAll("-", ""),
        service: rowData.serviceCode,
        batch: rowData.batchName,
        row: rowData.rowNumber,
        result: rowData.analysisResult,
        too6_pred: rowData.too6Pred,
        too5_pred: rowData.too5Pred
    }
}
function searchResultToRowData(analysis: Analysis): Object {
    return {
        id: analysis.batch+"$"+analysis.row+"$"+analysis.sample+"$"+analysis.service,
        batchName: analysis.batch,
        rowNumber: analysis.row,
        sampleId: formatingSampleId(analysis.request.sample.id.toString()),
        customerName: analysis.request.sample.patient.customer,
        customerId: analysis.request.sample.remark,
        serviceCode: analysis.request.service.id,
        serviceName: analysis.request.service.name,
        patientName: analysis.request.sample.patient.name,
        patientSex: analysis.request.sample.patient.sex,
        analysisResult: analysis.result,
        patientMrn: analysis.request.sample.patient.mrn,
        requestDate: analysis.request.date_request.toString().split("T")[0],
        requestTat: analysis.request.date_due.toString().split("T")[0],
        reportName: analysis.report.file_name,
        reportCreateAt: analysis.report.create_at,
        reportCreateBy: analysis.report.create_by.name,
        reportResult: analysis.report.report_result,
        reportResultType: analysis.report.report_result_type,
        reportComment: analysis.report.report_comment,
        reportText: analysis.report.report_text,
        reportPublishAt: analysis.report.publish_at ? analysis.report.publish_at.toString().split("T")[0] : "",
        reportPublishBy: analysis.report.publish_by.name ? analysis.report.publish_by.name : "미배포",
        too5Pred: analysis.too5_pred,
        too5Fems: analysis.too5_fems_prob,
        too6Pred: analysis.too6_pred,
        too6Fems: analysis.too6_fems_prob,
        iscore: analysis.iscore,
        cadEnsembleProb: analysis.cad_ensemble_prob,
        freemixA: analysis.freemix,
        rawReadMillionA: analysis.raw_reads_millions,
        filteredReadsA: analysis.total_reads,
        dupRateA: analysis.dup_rate,
        gcA: analysis.gc,
        meanA: analysis.mean,
        medianA: analysis.median,
        qcA: analysis.qc,
        chrXCountA: analysis.chr_xcnt,
        chrXPropA: analysis.chr_xprop,
        chrYCountA: analysis.chr_ycnt,
        chrYPropA: analysis.chr_yprop,
        sexPredictA: analysis.pred_sex === analysis.request.sample.patient.sex ? "P":"F",
        sexAnalysisA: analysis.pred_sex,
        freemixB: analysis.freemix_tmp,
        rawReadMillionB: analysis.raw_reads_millions_tmp,
        filteredReadsB: analysis.total_reads_tmp,
        dupRateB: analysis.dup_rate_tmp,
        gcB: analysis.gc_tmp,
        meanB: analysis.mean_tmp,
        medianB: analysis.median_tmp,
        qcB: analysis.qc_tmp,
        chrXCountB: analysis.chr_xcnt_tmp,
        chrXPropB: analysis.chr_xprop_tmp,
        chrYCountB: analysis.chr_ycnt_tmp,
        chrYPropB: analysis.chr_yprop_tmp,
        sexPredictB: analysis.pred_sex_tmp === analysis.request.sample.patient.sex ? "P":"F",
        sexAnalysisB: analysis.pred_sex_tmp,
        interpretation: analysis.comment,
        description: analysis.report.description
    }
}
function convertPassOrFail(str: string): string{
    switch (str) {
        case "P": return "PASS";
        case "F": return "FAIL";
        default:  return "ERROR";
    }
}
function convertResultName(result: string): string {
    switch (result) {
        case "RISK":
            return "집중관리";
        case "GENERAL":
            return "일반관리";
        case "CONCERN":
            return "관심관리";
        case "ESO":
            return "식도암";
        case "HCC":
            return "간암";
        case "OV":
            return "난소암";
        case "colon":
            return "대장암";
        case "LuC":
            return "폐암";
        case "Panc":
            return "췌장암";
        case "Others":
            return "기타암";
        default:
            return "미분류";
    }
}
function setColor(result: string): string {
    switch (result) {
        case "GENERAL":
            return "#8DC556";
        case "CONCERN":
            return "#EFA718";
        case "RISK":
            return "#D9341D";
        case "LuC":
            return "#FF3846";
        case "ESO":
            return "#17B70D";
        case "colon":
            return "#1E29C4";
        case "Panc":
            return "#BC0D18";
        case "HCC":
            return "#AD8105"
        case "OV":
            return "#E06F07"
        case "Others":
            return "#000000"
        case "P":
            return "#46BF26"
        case "F":
            return "#F25349"
        default:
            return "#FFFFFF";
    }
}
function formatingSampleId(input: string): string {
    const part1 = input.slice(0, 8);  // 20240826
    const part2 = input.slice(8, 11); // 171
    const part3 = input.slice(11);    // 5517

    return `${part1}-${part2}-${part3}`;
}

function isValidDate(dateString: string): boolean {
    const date = new Date(dateString)
    return !isNaN(date.getTime())
}
function isOnlySinglePrintable(selectedAnalysis: SelectedAnalysis[]): boolean {
    return selectedAnalysis.filter(value=>value.reportCreateAt !== "null").length !== 0 && selectedAnalysis.length >= 2
}
function isSinglePrintable(selectedAnalysis: SelectedAnalysis[]): boolean {
    return selectedAnalysis.filter(value=>value.reportCreateAt !== "null").length !== 0 && selectedAnalysis.length === 1
}
function isMultiPrintable(selectedAnalysis: SelectedAnalysis[]): boolean {
    return selectedAnalysis.filter(value => value.reportCreateAt === "null").length > 0
}
function isPublisable(selectedAnalysis: SelectedAnalysis[]): boolean {
    return selectedAnalysis.length !== 0
}
function hasPrintingReport(selectedAnalysis: SelectedAnalysis[]): boolean {
    return selectedAnalysis.filter(value=>value.reportCreateAt !== "null" && value.reportName === null).length > 0
}
const utils = {
    apiResponseToSearchResult,
    convertPredicatesToQueryString,
    convertResultName,
    convertPassOrFail,
    convertRowDataToSavableAnalysis,
    isValidDate,
    setColor,
    searchResultToRowData,
    isOnlySinglePrintable,
    isSinglePrintable,
    isMultiPrintable,
    isPublisable,
    hasPrintingReport
}
export default utils;
