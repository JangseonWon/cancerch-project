import {
    Box, Button, Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    OutlinedInput,
    TextField
} from "@mui/material";
import React, {useEffect, useState} from "react";
import useSnackBarStore from "../../common/stores/SnackBarStore";
import useHistoryDialogStore from "../stores/ReportHistoryStore";
import {ReportPrintAPI} from "../../common/utils/fetch";
import useLoadingStore from "../../common/stores/LoadingStore";

function HistoryDialog() {
    const [historyDialog, setHistoryDialog] = [useHistoryDialogStore(state => state.historyDialog), useHistoryDialogStore(state => state.setHistoryDialog)]
    const [history, setHistory] = useState<string>('')
    const [setLoading] = [useLoadingStore(state => state.setOpen)]
    const [setSnackbar] = [useSnackBarStore(state => state.setSnackbar)]
    const [historySavable, setHistorySavable] = useState<boolean>(true)

    useEffect(() => {
        setHistorySavable(true)
    }, [historyDialog.open])

    function historyOnClose() {
        setHistoryDialog({...historyDialog, open: false})
        setHistory("")
        setHistorySavable(true)
    }

    function historyOnPrint() {
        (async () => {
            await ReportPrintAPI(historyDialog.sampleId, historyDialog.serviceCode, historyDialog.batchName, historyDialog.rowNumber, history).then(value => {
                    setHistoryDialog({...historyDialog, open: false})
                    setHistory("")
                    setHistorySavable(true)
                }
            )
        })()
    }

    function handleHistoryOnChange(evt: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        setHistory(evt.target.value)
        if (history.length === 0) setHistorySavable(true)
        else setHistorySavable(false)
    }

    return (
        <Dialog
            open={historyDialog.open}
            onClose={historyOnClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description">
            <DialogTitle id="alert-dialog-title">
                소견 입력
            </DialogTitle>
            <DialogContent>
                <p>아래 환자의 소견정보를 입력합니다.</p>
                <Box display="flex" alignItems="left">
                    <FormControl variant="outlined" size="small">
                        <InputLabel htmlFor="originSampleId">의뢰번호</InputLabel>
                        <OutlinedInput
                            id="originSampleId"
                            type="string"
                            label="의뢰번호"
                            value={historyDialog.sampleId}
                            disabled
                            sx={{
                                borderTopRightRadius: 0,
                                borderBottomRightRadius: 0,
                                minWidth: '200px'
                            }}
                        />
                    </FormControl>
                    <FormControl variant="outlined" size="small">
                        <InputLabel htmlFor="to-date">검사코드</InputLabel>
                        <OutlinedInput
                            id="originServiceCode"
                            type="string"
                            label="검사명"
                            disabled
                            value={historyDialog.serviceName}
                            sx={{
                                boxShadow: 'none',
                                borderTopLeftRadius: 0,
                                borderBottomLeftRadius: 0,
                                minWidth: '100px'
                            }}
                        />
                    </FormControl>
                </Box>
                <Box display="flex" alignItems="left" sx={{paddingTop: '10px;', paddingBottom: '10px;'}}>
                    <FormControl variant="outlined" size="small">
                        <InputLabel htmlFor="originSampleId">의뢰번호</InputLabel>
                        <OutlinedInput
                            id="originSampleId"
                            type="string"
                            label="의뢰번호"
                            value={historyDialog.patientName}
                            disabled
                            sx={{width: "446px;"}}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <TextField label={"변경 사유"}
                               multiline
                               rows={4}
                               sx={{width: "446px;"}}
                               value={history}
                               onChange={(evt) => handleHistoryOnChange(evt)}>
                    </TextField>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button variant={"contained"} onClick={historyOnPrint}
                        disabled={historySavable}>PRINT</Button>
                <Button variant={"contained"} onClick={historyOnClose}>CANCEL</Button>
            </DialogActions>
        </Dialog>
    )
}

export default HistoryDialog
