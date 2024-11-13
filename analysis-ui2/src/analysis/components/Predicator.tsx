import {
    Box,
    Button,
    ButtonGroup,
    Dialog, DialogActions, DialogContent, DialogContentText,
    DialogTitle, FormControl,
    FormControlLabel, InputLabel, OutlinedInput,
    Switch,
} from "@mui/material";
import usePredicatesStore from "../stores/PredicatesStore";
import useSnackBarStore from "../../common/stores/SnackBarStore";
import React, {useEffect, useState} from "react";
import utils from "../../common/utils/Mapper";
import {GridSearchIcon} from "@mui/x-data-grid";
import useSelectedAnalysisStore from "../stores/SelectedAnalysisStore";
import useAlertDialogStore from "../../common/stores/AlertDialogStore";
import useConfirmDialogStore from "../../common/stores/ConfirmDialogStore";
import useSearchTriggerStore from "../stores/SearchTriggerStore";
import {
    LinkRequestValidationAPI,
    OriginRequestValidationAPI,
    ReportPrintAPI, ReportPublishAPI,
    RequestLinkAPI
} from "../../common/utils/fetch";
import {LinkDialogData} from "../../common/types/Types";
import useHistoryDialogStore from "../stores/ReportHistoryStore";

function Predicator() {
    const [predicates, setPredicates] = [usePredicatesStore(state => state.predicates), usePredicatesStore(state => state.setPredicates)]
    const [setSearchTrigger] = [useSearchTriggerStore(state => state.setTrigger)]
    const [setSnackbar] = [useSnackBarStore(state => state.setSnackbar)]
    const [setAlert] = [useAlertDialogStore(state => state.setDialog)]
    const [selectedAnalysis] = [useSelectedAnalysisStore(state => state.selectedAnalysis)]
    const [confirm, setConfirm] = [useConfirmDialogStore(state => state.dialog), useConfirmDialogStore(state => state.setDialog)]
    const [setHistory] = [useHistoryDialogStore(state => state.setHistoryDialog)]
    const [linkDialog, setLinkDialog] = useState<LinkDialogData>({
        originSampleId: "",
        originServiceCode: "",
        originBatchName: "",
        originRowNumber: "",
        linkSampleId: "",
        linkServiceCode: "",
        originValidation: false,
        linkValidation: true,
        linkable: true,
        open: false
    })
    const [printable, setPrintable] = useState<boolean>(true)
    const [publisable, setPublisable] = useState<boolean>(true)

    useEffect(() => {
        if (utils.isOnlySinglePrintable(selectedAnalysis)) {
            setPrintable(true)
        } else if (utils.hasPrintingReport(selectedAnalysis)) {
            setAlert({title: "사용자 알림", content: "선택 내역 중 출력 중인 결과지가 포함되어 있습니다.", open: true})
            setPrintable(true)
            setPublisable(true)
        } else if (utils.isSinglePrintable(selectedAnalysis)) {
            setPrintable(false)
        } else if (utils.isMultiPrintable(selectedAnalysis)) {
            setPrintable(false)
        } else {
            setPrintable(true)
        }

        if (utils.isPublisable(selectedAnalysis)) setPublisable(false)
        else setPublisable(true)

    }, [selectedAnalysis])

    function handleToDateChange(value: string) {
        if (utils.isValidDate(value)) {
            let filter = predicates.filters
            filter.to = new Date(value)
            setPredicates({...predicates, filters: filter})
        }
    }

    function handleFromDateChange(value: string) {
        if (utils.isValidDate(value)) {
            let filter = predicates.filters
            filter.from = new Date(value)
            setPredicates({...predicates, filters: filter})
        }
    }

    function handlePassToggleChange(evt: React.ChangeEvent<HTMLInputElement>) {
        let filter = predicates.filters
        if (evt.target.checked) {
            setSnackbar({message: "QC FAIL을 제외하고 검색합니다", openType: "info", state: true})
            filter.pass = true
            setPredicates({...predicates, filters: filter})
        } else {
            setSnackbar({message: "QC FAIL을 포함하고 검색합니다", openType: "info", state: true})
            filter.pass = false
            setPredicates({...predicates, filters: filter})
        }
    }

    function handlePrintedToggleChange(evt: React.ChangeEvent<HTMLInputElement>) {
        let filter = predicates.filters
        if (evt.target.checked) {
            setSnackbar({message: "결과지가 생성된 의뢰를 제외하고 검색합니다", openType: "info", state: true})
            filter.printed = true
            setPredicates({...predicates, filters: filter})
        } else {
            setSnackbar({message: "결과지가 생성된 의뢰를 포함하고 검색합니다", openType: "info", state: true})
            filter.printed = false
            setPredicates({...predicates, filters: filter})
        }
    }

    function handlePublishedToggleChange(evt: React.ChangeEvent<HTMLInputElement>) {
        let filter = predicates.filters
        if (evt.target.checked) {
            setSnackbar({message: "결과지가 배포된 의뢰를 제외하고 검색합니다", openType: "info", state: true})
            filter.published = true
            setPredicates({...predicates, filters: filter})
        } else {
            setSnackbar({message: "결과지가 배포된 의뢰를 포함하고 검색합니다", openType: "info", state: true})
            filter.published = false
            setPredicates({...predicates, filters: filter})
        }
    }

    function handleSearchOnClick() {
        setSnackbar({
            message: `${predicates.filters.from.toISOString().split("T")[0]}부터 ${predicates.filters.to.toISOString().split("T")[0]}까지 의뢰를 조회합니다.`,
            openType: "info",
            state: true
        })
        setSearchTrigger(true)
    }

    function handlePrintOnClick() {
        if (selectedAnalysis.length === 0) setAlert({title: "사용자 확인 요청", content: "선택된 항목이 없습니다.", open: true})
        else if (selectedAnalysis.filter(value => value.reportCreateAt !== "null").length === 1) {
            setHistory({
                sampleId: selectedAnalysis[0].sampleId,
                serviceName: selectedAnalysis[0].serviceName,
                serviceCode: selectedAnalysis[0].serviceCode,
                patientName: selectedAnalysis[0].patientName,
                batchName: selectedAnalysis[0].batchName,
                rowNumber: selectedAnalysis[0].rowNumber,
                history: "",
                open: true
            })
        } else {
            setConfirm({
                title: "결과지 생성",
                content: `선택한 ${selectedAnalysis.length}개의 결과지를 생성합니다.`,
                open: true,
                okFunction: () => {
                    (async () => {
                        selectedAnalysis.forEach(analysis => {
                            ReportPrintAPI(analysis.sampleId, analysis.serviceCode, analysis.batchName, analysis.rowNumber)
                        })
                    })()
                }
            })
        }
    }

    function handlePublishOnClick() {
        if (selectedAnalysis.length === 0) setAlert({title: "사용자 확인 요청", content: "선택된 항목이 없습니다.", open: true})
        else {
            setConfirm({
                title: "결과지 전송",
                content: `선택한 ${selectedAnalysis.length}개의 결과지를 전송합니다.`,
                open: true,
                okFunction: () => {
                    (async () => {
                        selectedAnalysis.forEach(analysis => {
                            ReportPublishAPI(analysis.sampleId, analysis.serviceCode, new Date(analysis.reportCreateAt).getTime())
                        })
                    })()
                    setConfirm({...confirm, open: false})
                }
            })
            console.log(selectedAnalysis)
        }
    }

    function handleDataLinkOnClick() {
        setLinkDialog({
            originSampleId: "",
            originServiceCode: "",
            originBatchName: "",
            originRowNumber: "",
            linkSampleId: "",
            linkServiceCode: "",
            originValidation: false,
            linkValidation: true,
            linkable: true,
            open: true
        })
    }

    function handleDataLinkStartOnClick() {
        (async () => {
            await RequestLinkAPI({
                origin_sample: Number(linkDialog.originSampleId),
                origin_service: linkDialog.originServiceCode,
                batch: linkDialog.originBatchName,
                row: Number(linkDialog.originRowNumber),
                link_sample: Number(linkDialog.linkSampleId),
                link_service: linkDialog.linkServiceCode
            }).then(value => {
                if (value) {
                    setLinkDialog({...linkDialog, open: false})
                    setSearchTrigger(true)
                    setSnackbar({message: "연동 완료했습니다", state: true, openType: "success"})
                } else {
                    setAlert({title: "사용자 확인 요청", content: "연동에 실패했습니다.", open: true})
                }
            })
        })()
    }

    function handleOriginRequestValidation() {
        (async () => {
            await OriginRequestValidationAPI(linkDialog.originSampleId, linkDialog.originServiceCode, linkDialog.originBatchName, linkDialog.originRowNumber).then(value => {
                if (value) {
                    setLinkDialog({...linkDialog, originValidation: true, linkValidation: false})
                } else {
                    setAlert({title: "사용자 확인 요청", content: "해당 분석결과가 없습니다.", open: true})
                }
            })
        })()

    }

    function handleLinkRequestValidation() {
        (async () => {
            await LinkRequestValidationAPI(linkDialog.linkSampleId, linkDialog.linkServiceCode).then(value => {
                if (value) {
                    setLinkDialog({...linkDialog, linkValidation: true, linkable: false})
                } else {
                    setAlert({title: "사용자 확인 요청", content: "해당 의뢰정보가 없습니다.", open: true})
                }
            })
        })()
    }


    const linkDialogClose = () => {
        setLinkDialog({
            originSampleId: "",
            originServiceCode: "",
            originBatchName: "",
            originRowNumber: "",
            linkSampleId: "",
            linkServiceCode: "",
            originValidation: false,
            linkValidation: true,
            linkable: true,
            open: false
        })
    }


    return (
        <>
            <div className="analysis__controller">
                <FormControlLabel control={<Switch checked={predicates.filters.pass}
                                                   onChange={(evt) => handlePassToggleChange(evt)}/>}
                                  label={"PASS ONLY"}/>
                <FormControlLabel control={<Switch checked={predicates.filters.printed}
                                                   onChange={(evt) => handlePrintedToggleChange(evt)}/>}
                                  label={"결과지 미생성"}/>
                <FormControlLabel control={<Switch checked={predicates.filters.published}
                                                   onChange={(evt) => handlePublishedToggleChange(evt)}/>}
                                  label={"결과지 미배포"}/>
                <Box display="flex" alignItems="center">
                    <FormControl variant="outlined" size="small">
                        <InputLabel htmlFor="from-date">Date from</InputLabel>
                        <OutlinedInput
                            id="from-date"
                            type="date"
                            label="Date from"
                            value={predicates.filters.from.toISOString().split("T")[0]}
                            onChange={(evt) => handleFromDateChange(evt.target.value)}
                            sx={{
                                borderTopRightRadius: 0,
                                borderBottomRightRadius: 0,
                            }}
                        />
                    </FormControl>
                    <FormControl variant="outlined" size="small">
                        <InputLabel htmlFor="to-date">Date to</InputLabel>
                        <OutlinedInput
                            id="to-date"
                            type="date"
                            label="Date to"
                            value={predicates.filters.to.toISOString().split("T")[0]}
                            onChange={(evt) => handleToDateChange(evt.target.value)}
                            sx={{
                                borderRadius: 0,
                            }}
                        />
                    </FormControl>
                    <Button
                        size="large"
                        variant="contained"
                        sx={{
                            boxShadow: 'none',
                            borderTopLeftRadius: 0,
                            borderBottomLeftRadius: 0,
                            minWidth: '50px',
                        }}
                        onClick={handleSearchOnClick}
                    >
                        <GridSearchIcon/>
                    </Button>
                </Box>
                <ButtonGroup size="large" sx={{marginLeft: '10px;'}}>
                    <Button onClick={handleDataLinkOnClick}>데이터 연동</Button>
                    <Button onClick={handlePrintOnClick} disabled={printable}>결과지 생성</Button>
                    <Button onClick={handlePublishOnClick} disabled={publisable}>결과지 전송</Button>
                </ButtonGroup>
                <Dialog
                    open={linkDialog.open}
                    onClose={linkDialogClose}
                    aria-labelledby="link-dialog-title"
                    aria-describedby="link-dialog-description">
                    <DialogTitle id="link-dialog-title">
                        데이터 연결
                    </DialogTitle>
                    <DialogContent>
                        <hr/>
                        <DialogContentText> 사용법 </DialogContentText>
                        <p>1. 원의뢰 결과정보 입력 후 검증</p>
                        <p>2. 연결의뢰번호와 검사코드(ex. N203) 입력 후 검증</p>
                        <p>3. 활성화 된 LINK 버튼 선택 후 작업 종료까지 대기</p>
                        <hr/>
                        <h4>원 의뢰정보</h4>
                        <Box display="flex" alignItems="left">
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="originSampleId">의뢰번호</InputLabel>
                                <OutlinedInput
                                    id="originSampleId"
                                    type="string"
                                    label="의뢰번호"
                                    value={linkDialog.originSampleId}
                                    disabled={linkDialog.originValidation}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, originSampleId: evt.target.value})
                                    }}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="to-date">검사코드</InputLabel>
                                <OutlinedInput
                                    id="originServiceCode"
                                    type="string"
                                    label="검사코드"
                                    disabled={linkDialog.originValidation}
                                    value={linkDialog.originServiceCode}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, originServiceCode: evt.target.value})
                                    }}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '50px',
                                        maxWidth: '150px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box display="flex" alignItems="left" sx={{marginTop: "10px;"}}>
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="originSampleId">배치명</InputLabel>
                                <OutlinedInput
                                    id="originSampleId"
                                    type="string"
                                    label="배치명"
                                    disabled={linkDialog.originValidation}
                                    value={linkDialog.originBatchName}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, originBatchName: evt.target.value})
                                    }}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="to-date">배치번호</InputLabel>
                                <OutlinedInput
                                    id="originServiceCode"
                                    type="string"
                                    label="배치번호"
                                    value={linkDialog.originRowNumber}
                                    disabled={linkDialog.originValidation}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, originRowNumber: evt.target.value})
                                    }}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '50px',
                                        maxWidth: '150px'
                                    }}
                                />
                            </FormControl>
                            <Button variant={"outlined"} sx={{marginLeft: '10px;', minWidth: "130px;"}}
                                    disabled={linkDialog.originValidation} onClick={handleOriginRequestValidation}>원의뢰
                                검증</Button>
                        </Box>
                        <hr/>
                        <h4>연결 의뢰정보</h4>
                        <Box display="flex" alignItems="left" sx={{marginTop: "10px;"}}>
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="to-date">의뢰번호</InputLabel>
                                <OutlinedInput
                                    id="linkSampleId"
                                    type="string"
                                    label="의뢰번호"
                                    value={linkDialog.linkSampleId}
                                    disabled={linkDialog.linkValidation}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, linkSampleId: evt.target.value})
                                    }}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined" size="small">
                                <InputLabel htmlFor="to-date">검사코드</InputLabel>
                                <OutlinedInput
                                    id="linkServiceCode"
                                    type="string"
                                    label="배치번호"
                                    value={linkDialog.linkServiceCode}
                                    disabled={linkDialog.linkValidation}
                                    onChange={(evt) => {
                                        setLinkDialog({...linkDialog, linkServiceCode: evt.target.value})
                                    }}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '50px',
                                        maxWidth: '150px'
                                    }}
                                />
                            </FormControl>
                            <Button variant={"outlined"} sx={{marginLeft: '10px;', minWidth: "130px;"}}
                                    disabled={linkDialog.linkValidation} onClick={handleLinkRequestValidation}>연결의뢰
                                검증</Button>
                        </Box>
                    </DialogContent>
                    <DialogActions>
                        <Button variant={"contained"} sx={{boxShadow: 'none'}} disabled={linkDialog.linkable}
                                onClick={handleDataLinkStartOnClick}
                        >LINK</Button>
                        <Button onClick={linkDialogClose}>
                            CLOSE
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        </>
    )
}

export default Predicator


