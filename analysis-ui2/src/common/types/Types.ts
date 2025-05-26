import {AlertColor} from "@mui/material";

interface Analysis {
    batch: string
    cad_ensemble_prob: number
    chr_xcnt: number
    chr_xcnt_tmp: number
    chr_xprop: number
    chr_xprop_tmp: number
    chr_ycnt: number
    chr_ycnt_tmp: number
    chr_yprop: number
    chr_yprop_tmp: number
    comment: string
    created_at: Date
    dup_rate: number
    dup_rate_tmp: number
    file: string | null
    freemix: number
    freemix_tmp: number
    gc: number
    gc_tmp: number
    iscore: number
    last_modify_at: Date
    last_modify_by: User
    mean: number
    mean_tmp: number
    median: number
    median_tmp: number
    pred_sex: string
    pred_sex_tmp: string
    qc: string
    qc_tmp: string
    raw_reads_millions: number
    raw_reads_millions_tmp: number
    report: Report
    request: Request
    result: string
    row: number
    sample: number
    service: string
    too5_fems_prob: number
    too5_pred: string
    too6_fems_prob: number
    too6_pred: string
    total_reads: number
    total_reads_tmp: number
    iscore_path: string
    fems_bc: number
    cov_bc: number
    fems_cov_bc: number
    fems_cov_bernn: number
    fems_path: string
    clinical_cancer: string
    language: string
    cf_dna_concentration: number
}

interface Request {
    canceled: string
    date_due: Date
    date_request: Date
    date_sampling: Date
    date_start: Date
    deleted: string
    registered: string
    sample: Sample
    service: Service
}

interface Sample {
    barcode: string
    id: number
    patient: Patient
    remark: string | null
}

interface Patient {
    birth: Date
    customer: string
    mrn: string
    name: string
    sex: string
}

interface Service {
    id: string
    name: string
}

interface Report {
    sample: number
    service: string
    create_at: Date
    create_by: User
    description: string
    file_name: string | null
    file_size: number | null
    file_url: string | null
    is_printed: string | null
    publish_at: Date | null
    publish_by: User
    report_result: String | null
    report_result_type: String | null
    report_comment: String | null
    report_text: String | null
}

interface SearchResult {
    data: Analysis[]
    current_page: number
    total_page: number
}

interface User {
    id: string
    name: string
}

interface SnackBarData {
    message: string
    openType: AlertColor
    state: boolean
}

interface AlertData {
    title: string
    content: string
    open: boolean
}

interface ConfirmData {
    title: string
    content: string
    open: boolean
    okFunction: () => void
}

interface LinkDialogData {
    open: boolean
    originSampleId: string
    originServiceCode: string
    originBatchName: string
    originRowNumber: string
    originValidation: boolean
    linkSampleId: string
    linkServiceCode: string
    linkValidation: boolean
    linkable: boolean
}

interface InterpretationDialogData {
    open: boolean
    sampleId: string
    serviceCode: string
    serviceName: string
    batchName: string
    rowNumber: string
    patientName: string
    interpretation: string
}

interface ReportDialogData {
    open: boolean
    sampleId: string,
    serviceCode: string
    serviceName: string
    batchName: string
    rowNumber: string
    customerName: string
    customerId: string
    patientName: string
    patientMrn: string
    patientSex: string
    reportCreateAt: string
    reportCreateBy: string
    description: string
    reportName: string | null
    fileUrl: string
    reportPublishAt: string | null
    reportPublishBy: string | null
    reportResult: string | null
    reportResultType: string | null
    reportComment: string | null
    reportText: string | null
}

interface HistoryDialogData {
    open: boolean
    sampleId: string
    serviceCode: string
    serviceName: string
    batchName: string
    rowNumber: number
    patientName: string
    history: string
}

interface LoadingObject {
    onLoading: boolean
    loadingText: string
}

interface AnalysisPredicates {
    page: number
    limit: number
    sort_by: string
    asc: boolean
    filters: AnalysisPredicatesFilter
}

interface AnalysisPredicatesFilter {
    to: Date
    from: Date
    published: boolean
    printed: boolean
    pass: boolean
}

interface SelectedAnalysis {
    sampleId: string
    serviceCode: string
    serviceName: string
    batchName: string
    rowNumber: number
    patientName: string
    reportCreateAt: string
    reportName: string
}

interface SavableAnalysis {
    sampleId: string,
    serviceCode: string,
    batchName: string,
    rowNumber: number,
    analysisResult: string,
    too6Pred: string,
    too5Pred: string,
    cfDnaConcentration: number
    iscore: number
    femsBc: number
    covBc: number
    femsCovBc: number
    femsCovBernn: number
}

export type {
    Analysis,
    AnalysisPredicates,
    AlertData,
    ConfirmData,
    LinkDialogData,
    InterpretationDialogData,
    SavableAnalysis,
    SearchResult,
    SelectedAnalysis,
    HistoryDialogData,
    LoadingObject,
    SnackBarData,
    ReportDialogData
}
