import {useEffect, useState} from 'react';
import {CircularProgress} from "@mui/material";

interface LoadingProps {
    onLoading: boolean,
    loadingText: string
}

function LoadingSpinner({ onLoading, loadingText } : LoadingProps) {
    const [loading, setLoading] = useState<string>("loading_container_none")
    useEffect(() => {
        if(onLoading) setLoading("loading_container")
        else setLoading("loading_container_none")
    }, [onLoading])
    return (
        <div className={loading}>
            <CircularProgress size={100}/>
            <h3>{loadingText}</h3>
        </div>
    )
}

export default LoadingSpinner;
