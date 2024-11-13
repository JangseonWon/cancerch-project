import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import React from "react";
import useConfirmDialogStore from "../stores/ConfirmDialogStore";

function ConfirmDialog() {
    const [confirm, setConfirm] = [useConfirmDialogStore(state => state.dialog), useConfirmDialogStore(state => state.setDialog)]
    const confirmDialogClose = () => {
        setConfirm({...confirm, open: false})
    }

    return (
        <Dialog
            open={confirm.open}
            onClose={confirmDialogClose}
            aria-labelledby="confirm-dialog-title"
            aria-describedby="confirm-dialog-description">
            <DialogTitle id="confirm-dialog-title">
                {confirm.title}
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="confirm-dialog-description">
                    {confirm.content}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button variant={"contained"} onClick={confirm.okFunction}>OK</Button>
                <Button onClick={confirmDialogClose}>
                    CLOSE
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default ConfirmDialog
