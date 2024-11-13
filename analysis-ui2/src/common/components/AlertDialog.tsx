import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import React from "react";
import useAlertDialogStore from "../stores/AlertDialogStore";

function ALertDialog() {
    const [alert, setAlert] = [useAlertDialogStore(state => state.dialog), useAlertDialogStore(state => state.setDialog)]
    const alertDialogClose = () => {
        setAlert({...alert, open: false})
    }

    return (
        <Dialog
            open={alert.open}
            onClose={alertDialogClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description">
            <DialogTitle id="alert-dialog-title">
                {alert.title}
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {alert.content}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button variant={"contained"} onClick={alertDialogClose}>
                    확인
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default ALertDialog
