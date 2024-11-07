import React, {useEffect, useState} from "react";
import useLoadingStore from "../common/stores/LoadingStore";
import useSnackBarStore from "../common/stores/SnackBarStore";
import usePredicatesStore from "./stores/PredicatesStore";
import {AnalysisSearchAPI, ReportPublishEvent, ReportQueueEvent} from "../common/utils/fetch";
import {
    Alert,
    Breadcrumbs,
    Link,
    Slide,
    Snackbar,
    Typography
} from "@mui/material";
import {
    DoubleArrow, HomeSharp, LensBlur
} from "@mui/icons-material";
import LoadingSpinner from "../common/components/LoadingSpinner";
import {SearchResult} from "../common/types/Types";
import DataComponent from "./components/DataComponent";
import Predicator from "./components/Predicator";
import utils from "../common/utils/Mapper";
import useSearchTriggerStore from "./stores/SearchTriggerStore";
import InterpretationDialog from "./components/InterpretationDialog";

function AnalysisPage() {
    const [analysisData, setAnalysisData] = useState<SearchResult | null>()
    const [loading, setLoading] = [useLoadingStore(state => state.open), useLoadingStore(state => state.setOpen)]
    const [snackbar, setSnackbar] = [useSnackBarStore(state => state.open), useSnackBarStore(state => state.setSnackbar)]
    const [predicates, setPredicates] = [usePredicatesStore(state => state.predicates), usePredicatesStore(state => state.setPredicates)]
    const [searchTrigger, setSearchTrigger] = [useSearchTriggerStore(state => state.trigger), useSearchTriggerStore(state => state.setTrigger)]
    useEffect(() => {
        const initializeSSE = async () => {
            setLoading({ onLoading: true, loadingText: "결과지 생성/관리 모듈과 연결 중입니다." });

            try {
                const reportQueueSSE = await ReportQueueEvent();

                reportQueueSSE.addEventListener("CREATE", (event: MessageEvent) => {
                    const data = JSON.parse(event.data);
                    console.log(data)
                    setSearchTrigger(true);
                })
                reportQueueSSE.addEventListener("PRINTING",(event: MessageEvent) => {
                    const data = JSON.parse(event.data);
                    setSnackbar({ message: `${data.id.split("$")[0]} 결과지가 출력중입니다.`, openType: "info", state: true });
                })
                reportQueueSSE.addEventListener("FINISH", (event: MessageEvent) => {
                    setSearchTrigger(true);
                })
                reportQueueSSE.onerror = (event) => {
                    console.log(event);
                    setSnackbar({ message: `결과지 생성 모듈에 오류가 발생했습니다. 재연결 중입니다.`, openType: "error", state: true });
                };

                const reportPublishSSE = await ReportPublishEvent();
                reportPublishSSE.addEventListener("PUBLISH", (event: MessageEvent) => {
                    const data = JSON.parse(event.data);
                    setSnackbar({ message: `${data.id.split("$")[0]} 결과지가 전송됐습니다.`, openType: "success", state: true });
                    setSearchTrigger(true);
                })
                reportPublishSSE.onerror = (event) => {
                    console.log(event);
                    setSnackbar({ message: `결과지 전송 모듈에 오류가 발생했습니다.`, openType: "error", state: true });
                };
            } catch (error) {
                console.error("SSE Initialization Error:", error);
            }
        };

        initializeSSE()
    }, []);

    useEffect(() => {
        (async () => {
            setLoading({onLoading: true, loadingText: "분석 결과를 가져오고 있습니다."})
            const apiSearchResult = await AnalysisSearchAPI(utils.convertPredicatesToQueryString(predicates))
            setAnalysisData(apiSearchResult)
            setLoading({onLoading: false, loadingText: ""})
            setSearchTrigger(false)
        })()
    }, [searchTrigger]);
    const snackbarClose = () => {
        setSnackbar({...snackbar, state: false});
    }
    return (
        <>
            <div className={"analysis"}>
                <div className={"analysis__header"}>
                    <div style={{display: "flex"}}>
                        <div className={"icon__border"}><LensBlur fontSize="large"/></div>
                        <div>
                            <Typography variant="h5">ANALYSIS</Typography>
                            <Breadcrumbs aria-label="breadcrumb"
                                         separator={<DoubleArrow/>}>
                                <HomeSharp sx={{display: "flex"}}/>
                                <Link
                                    underline="hover"
                                    color="inherit"
                                    href="/analysis.html">
                                    CANCERCH
                                </Link>
                                <Link
                                    underline="hover"
                                    color="inherit"
                                    href="/analysis.html">
                                    ANALYSIS
                                </Link>
                            </Breadcrumbs>
                        </div>
                            {analysisData && <Predicator/>}
                    </div>
                </div>
                <div className={"analysis__body"}>
                    <div className={"analysis__grid"}>
                        {analysisData && <DataComponent searchResult={analysisData}/>}
                    </div>
                </div>
            </div>
            <LoadingSpinner onLoading={loading.onLoading} loadingText={loading.loadingText}/>
            <Snackbar open={snackbar.state} autoHideDuration={1000} onClose={snackbarClose} TransitionComponent={Slide}>
                <Alert onClose={snackbarClose} severity={snackbar.openType} sx={{width: '100%'}}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
            <InterpretationDialog/>
        </>
    )
}

export default AnalysisPage
