import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl, InputLabel,
    OutlinedInput,
    TextField, Typography
} from "@mui/material";
import useReportDialogStore from "../stores/ReportDialogStore";
import React, {useEffect} from "react";

function ReportDialog() {
    const [reportDialog, setReportDialog] = [useReportDialogStore(state => state.reportDialog), useReportDialogStore(state => state.setReportDialog)]

    function handleReportDialogOnClose() {
        setReportDialog({...reportDialog, open: false})
    }

    return (<Dialog
            open={reportDialog.open}
            onClose={handleReportDialogOnClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
            fullScreen>
            <DialogTitle id="alert-dialog-title">
                결과지 생성 결과 조회
            </DialogTitle>
            <DialogContent>
                <Box sx={{display: "flex", height: "100%"}}>
                    <iframe src={reportDialog.fileUrl} style={{width: "60%", height: "100%"}}></iframe>
                    <hr/>
                    <Box sx={{display: "flex", flexDirection: "column", justifyContent: "space-evenly"}}>
                        <Box>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="originSampleId" size={"small"}>의뢰번호</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="sampleId"
                                    type="string"
                                    label="의뢰번호"
                                    value={reportDialog.sampleId}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>검사명</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="serviceName"
                                    type="string"
                                    label="검사명"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.serviceName}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="patientSex" size={"small"}>성별</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="patientSex"
                                    type="string"
                                    label="성별"
                                    value={reportDialog.patientSex}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '50px',
                                        width: '50px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>수진자명</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="patientName"
                                    type="string"
                                    label="수진자명"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.patientName}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '30px',
                                        width: '650px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="originSampleId" size={"small"}>거래처명</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="customerName"
                                    type="string"
                                    label="거래처명"
                                    value={reportDialog.customerName}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '100px',
                                        width: '233px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>등록번호</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="mrn"
                                    type="string"
                                    label="등록번호"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.patientMrn}
                                    sx={{
                                        boxShadow: 'none',
                                        borderRadius: 0,
                                        minWidth: '100px',
                                        width: '233px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>타기관의뢰번호</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="mrn"
                                    type="string"
                                    label="타기관의뢰번호"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.customerId}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '100px',
                                        width: '234px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="originSampleId" size={"small"}>분석 배치</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="batch"
                                    type="string"
                                    label="분석 배치"
                                    value={reportDialog.batchName}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>분석 순번</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="mrn"
                                    type="string"
                                    label="분석 순번"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.rowNumber}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box>
                            <TextField label={"결과지 명"} value={reportDialog.reportName} sx={{width: "700px;"}}
                                       inputProps={{readOnly: true}} size="small"/>
                        </Box>
                        <Box>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="originSampleId" size={"small"}>입력 결과</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="customerName"
                                    type="string"
                                    label="입력 결과"
                                    value={reportDialog.reportResult}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>예측 암종</InputLabel>
                                <OutlinedInput
                                    size="small"
                                    id="mrn"
                                    type="string"
                                    label="예측 암종"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.reportResultType}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '100px',
                                        width: '350px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                        <Box>
                            <TextField multiline rows={3} label={"입력 소견"} type={"string"}
                                       value={reportDialog.reportComment}
                                       sx={{width: "700px;"}} inputProps={{readOnly: true, sx: {fontSize: "12px"}}}/>
                        </Box>
                        <Box>
                            <TextField label={"서술형 결과지"} multiline rows={8} sx={{width: "700px;"}}
                                       value={reportDialog.reportText} inputProps={{readOnly: true, sx: {fontSize: "12px"}}}></TextField>
                        </Box>
                        <Box>
                            <TextField multiline rows={2} label={"이력 작성 내용"} type={"string"}
                                       value={reportDialog.description} inputProps={{readOnly: true, sx: {fontSize: "12px"}}}
                                       sx={{width: "700px"}}/>
                        </Box>
                        <Box display="flex" alignItems="left">
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="originSampleId" size={"small"}>생성자</InputLabel>
                                <OutlinedInput
                                    id="originSampleId"
                                    type="string"
                                    label="생성자"
                                    size="small"
                                    value={reportDialog.reportCreateBy}
                                    inputProps={{readOnly: true}}
                                    sx={{
                                        borderTopRightRadius: 0,
                                        borderBottomRightRadius: 0,
                                        minWidth: '100px',
                                        width: '175px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>생성일</InputLabel>
                                <OutlinedInput
                                    id="originServiceCode"
                                    type="string"
                                    label="생성일"
                                    size="small"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.reportCreateAt}
                                    sx={{
                                        boxShadow: 'none',
                                        borderRadius: 0,
                                        minWidth: '100px',
                                        width: '175px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>배포자</InputLabel>
                                <OutlinedInput
                                    id="reportPublishBy"
                                    type="string"
                                    label="배포자"
                                    size="small"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.reportPublishBy}
                                    sx={{
                                        boxShadow: 'none',
                                        borderRadius: 0,
                                        minWidth: '100px',
                                        width: '175px'
                                    }}
                                />
                            </FormControl>
                            <FormControl variant="outlined">
                                <InputLabel htmlFor="to-date" size={"small"}>배포일</InputLabel>
                                <OutlinedInput
                                    id="reportPublishAt"
                                    type="string"
                                    label="배포일"
                                    size="small"
                                    inputProps={{readOnly: true}}
                                    value={reportDialog.reportPublishAt}
                                    sx={{
                                        boxShadow: 'none',
                                        borderTopLeftRadius: 0,
                                        borderBottomLeftRadius: 0,
                                        minWidth: '100px',
                                        width: '175px'
                                    }}
                                />
                            </FormControl>
                        </Box>
                    </Box>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button variant={"contained"} onClick={handleReportDialogOnClose}>CANCEL</Button>
            </DialogActions>
        </Dialog>
    )
}

export default ReportDialog
