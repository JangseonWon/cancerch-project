import axios, {AxiosRequestConfig} from "axios";
import {
    Analysis
} from "../types/Types";
import utils from "./Mapper";
import {errorHandler} from "./errorHandler";

const getConfig: AxiosRequestConfig = JSON.parse(import.meta.env.VITE_CONFIG_GET_DATA)
const postConfig: AxiosRequestConfig<string[]> = JSON.parse(import.meta.env.VITE_CONFIG_POST_DATA)

const axiosInstance = axios.create(postConfig)

interface postMessage {
    ___id: string,
    type: string,
    param: string
}

function generateRandomString(length: number): string {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';

    for (let i = 0; i < length; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters.charAt(randomIndex);
    }
    return result;
}

const fetchData = async (requestURL: string): Promise<string> => {
    return new Promise((resolve, reject) => {
        const randomId = generateRandomString(10)
        const messageData = {___id: randomId, type: "REQUEST", param: requestURL}
        const handleMessage = (event: MessageEvent) => {
            if (event === null) return;
            if (event.data === null) return;

            const data: postMessage = JSON.parse(event.data)

            if (data.___id === null) return;
            if (data.___id !== messageData.___id) return;
            if (data.type === "RESPONSE") {
                window.removeEventListener('message', handleMessage);
                resolve(data.param)
            }
        };
        window.addEventListener('message', handleMessage);
        parent.postMessage(JSON.stringify(messageData), '*');
    });
}

const getApiPath = async (path: string) : Promise<string> => {
    if (import.meta.env.DEV) {
        if (path.startsWith("/analysis")) path = "http://localhost:62179" + path
        else if (path.startsWith("/report")) path = "http://localhost:55398" + path
        else if (path.startsWith("/publish")) path = "http://localhost:29486" + path
        else path
    }
    else if (!path.startsWith("http")) path = await fetchData(path)
    return path
}

const _analysisSearchAPI = async (payload: string) => {
    const path = await getApiPath("/analysis/search?"+payload)
    const response = await axiosInstance.get<Analysis[]>(path)
    return utils.apiResponseToSearchResult(response.headers, response.data)
}

const _reportPreviewAPI = async (sample: number, service: string, createAt: number) => {
    const path = await getApiPath(`/report/samples/${sample}/services/${service}/reports/${createAt}`)
    const response = await axiosInstance.get<ArrayBuffer>(path, {...postConfig, responseType: 'arraybuffer'})
    return response.data
}

const _reportPrintAPI = async (sample: number, service: string, batchName: string, rowNumber: number) => {
    const path = await getApiPath(`/report/samples/${sample}/services/${service}/batch/${batchName}/row/${rowNumber}/print/kokr`)
    const response = await axiosInstance.put<void>(path)
    return response.status
}

const _reportPublishAPI = async (sample: number, service: string, createAt: number) => {
    const path = await getApiPath(`/publish/samples/${sample}/services/${service}/reports/${createAt}/publish`)
    return (await axiosInstance.put<void>(path)).status
}

const _reportQueueSSE = async () => {
    const path = await getApiPath("/report/queue");
    return new EventSource(path)
};

const _reportPublishSSE = async () => {
    const path = await getApiPath("/publish/publishoutcome");
    return new EventSource(path)
};
const _analysisInterpretationAPI = async (sample: string, service: string, batchName: string, rowNumber: string, interpretation: string) => {
    const path = await getApiPath(`/analysis/${sample}/${service}/${batchName}/${rowNumber}/comment`)
    return (await axiosInstance.patch(path, interpretation)).status
}
const _analysisChangeUpdateAPI = async(analysis: object) => {
    const path = await getApiPath('/analysis/update')
    return (await axiosInstance.patch(path, analysis)).status
}
const _originRequestValidationAPI = async(sample: string, service: string, batchName: string, rowNumber: string) : Promise<boolean>=> {
    const path = await getApiPath(`/analysis/validate/${sample}/${service}/${batchName}/${rowNumber}`)
    return (await axiosInstance.get(path)).data
}
const _linkRequestValidationAPI = async(sample: string, service: string) : Promise<boolean> => {
    const path = await getApiPath(`/analysis/request/${sample}/${service}`)
    return (await axiosInstance.get(path)).data
}
const _requestLinkAPI = async(linkRequest: {origin_sample: number, origin_service: string, batch: string, row: number, link_sample: number, link_service: string}): Promise<boolean> => {
    const path = await getApiPath(`/analysis/linkData`)
    return (await axiosInstance.post(path, linkRequest)).data
}

export const AnalysisSearchAPI = errorHandler(_analysisSearchAPI)
export const AnalysisUpdateAPI = errorHandler(_analysisChangeUpdateAPI)
export const ReportPreviewAPI = errorHandler(_reportPreviewAPI)
export const ReportPrintAPI = errorHandler(_reportPrintAPI)
export const ReportPublishAPI = errorHandler(_reportPublishAPI)
export const ReportQueueEvent = errorHandler(_reportQueueSSE)
export const ReportPublishEvent = errorHandler(_reportPublishSSE)
export const InterpretationAPI = errorHandler(_analysisInterpretationAPI)
export const OriginRequestValidationAPI = errorHandler(_originRequestValidationAPI)
export const LinkRequestValidationAPI = errorHandler(_linkRequestValidationAPI)
export const RequestLinkAPI = errorHandler(_requestLinkAPI)
