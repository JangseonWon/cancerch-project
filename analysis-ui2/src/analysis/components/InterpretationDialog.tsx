import {
    Box, Button,
    Dialog, DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    OutlinedInput,
    TextField
} from "@mui/material";
import React, {useEffect, useState} from "react";
import useInterpretationDialogStore from "../stores/InterpretationStore";
import {InterpretationAPI} from "../../common/utils/fetch";
import useSnackBarStore from "../../common/stores/SnackBarStore";
import useSearchTriggerStore from "../stores/SearchTriggerStore";

function InterpretationDialog() {
    const [interpretationDialog, setInterpretationDialog] = [useInterpretationDialogStore(state => state.interpretationDialog), useInterpretationDialogStore(state=>state.setInterpretationDialog)]
    const [interpretation, setInterpretation] = useState<string>('')
    const [setSearchTrigger] = [useSearchTriggerStore(state => state.setTrigger)]
    const [setSnackbar] = [useSnackBarStore(state => state.setSnackbar)]
    const [interpretationSable, setInterpretationSable] = useState<boolean>(true)

    useEffect(()=>{
        setInterpretation(interpretationDialog.interpretation)
        setInterpretationSable(true)
    }, [interpretationDialog.open])
    function handleInterpretationOnClose() {
        setInterpretationDialog({...interpretationDialog, open: false})
        setInterpretation("")
        setInterpretationSable(true)
    }

    function handleInterpretationOnSave() {
        (async () => {
            if(await InterpretationAPI(interpretationDialog.sampleId, interpretationDialog.serviceCode, interpretationDialog.batchName, interpretationDialog.rowNumber, interpretation) === 200) {
                setInterpretationDialog({...interpretationDialog, open: false})
                setInterpretation("")
                setSearchTrigger(true)
                setInterpretationSable(true)
                setSnackbar({message: "소견 저장을 완료했습니다.", state: true, openType: "success"})
            } else setSnackbar({message: "소견 저장에 실패했습니다.", state: true, openType: "error"})
        })()

    }

    function handleInterpretationOnChange(evt: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        setInterpretation(evt.target.value)
        setInterpretationSable(false)
    }

    return (
        <Dialog
            open={interpretationDialog.open}
            onClose={handleInterpretationOnClose}
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
                            value={interpretationDialog.sampleId}
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
                            value={interpretationDialog.serviceName}
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
                            value={interpretationDialog.patientName}
                            disabled
                            sx={{width:"446px;"}}
                        />
                    </FormControl>
                </Box>
                <Box>
                    <TextField label={"소견"}
                               multiline
                               rows={4}
                               sx={{width:"446px;"}}
                               value={interpretation}
                               onChange={(evt)=>handleInterpretationOnChange(evt)}>
                    </TextField>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button variant={"contained"} onClick={handleInterpretationOnSave} disabled={interpretationSable}>OK</Button>
                <Button variant={"contained"} onClick={handleInterpretationOnClose}>CANCEL</Button>
            </DialogActions>
        </Dialog>
    )
}

export default InterpretationDialog
