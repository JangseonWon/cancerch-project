import React, {useEffect, useState} from "react";
import {
    DataGrid, GridActionsCellItem,
    GridColDef,
    GridRenderEditCellParams,
    GridRowId,
    GridRowsProp,
    useGridApiRef
} from "@mui/x-data-grid";
import utils from "../../common/utils/Mapper";
import {
    MenuItem, Select, SelectChangeEvent
} from "@mui/material";
import SummarizeIcon from '@mui/icons-material/Summarize';
import {SearchResult} from "../../common/types/Types";
import {AnalysisUpdateAPI, ReportPreviewAPI} from "../../common/utils/fetch";
import useSelectedAnalysisStore from "../stores/SelectedAnalysisStore";
import EditNoteIcon from '@mui/icons-material/EditNote';
import useInterpretationDialogStore from "../stores/InterpretationStore";
import useSearchTriggerStore from "../stores/SearchTriggerStore";
import useSnackBarStore from "../../common/stores/SnackBarStore";

interface DataComponentProps {
    searchResult: SearchResult
}

export default function DataComponent(props: DataComponentProps) {
    const [paginationModel, setPaginationModel] = React.useState({
        pageSize: 100,
        page: 0,
    });
    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowSelectionModel, setRowSelectionModel] = useState<GridRowId[]>([]);
    const [setInterpretationDialog] = [useInterpretationDialogStore(state=>state.setInterpretationDialog)]
    const [setSearchTrigger] = [useSearchTriggerStore(state => state.setTrigger)]
    const [setSnackbar] = [useSnackBarStore(state => state.setSnackbar)]
    const [setSelectedAnalysis] = [useSelectedAnalysisStore(state => state.setSelectedAnalysis)]
    const apiRef = useGridApiRef();

    useEffect(() => {
        setRows(props.searchResult.data.map(utils.searchResultToRowData))
    }, [props])

    function selectableColumnOnChange(params: GridRenderEditCellParams, event: SelectChangeEvent<unknown>) {
        (async() => {
            let newRow = {...params.row, [params.field]: event.target.value}
            params.api.stopCellEditMode({id: params.id, field: params.field});
            if(await AnalysisUpdateAPI(utils.convertRowDataToSavableAnalysis(newRow)) === 200) {
                setSearchTrigger(true)
            }
        })()
    }

    const columns: GridColDef[] = [
        {field: 'batchName', headerName: '배치명', width: 100, align: "left"},
        {field: 'rowNumber', headerName: '순번', width: 80, align: "center"},
        {field: 'sampleId', headerName: '지놈의뢰번호', width: 120},
        {field: 'customerId', headerName: '타기관의뢰번호', width: 120},
        {field: 'serviceName', headerName: '검사명', width: 135},
        {field: 'patientName', headerName: '수진자명', width: 135},
        {field: 'patientMrn', headerName: 'MRN', width: 100},
        {field: 'patientSex', headerName: '성별', width: 70, align: "center"},
        {field: 'sexPredictA', headerName: "성별 분석 A", align: "center", width: 80},
        {field: 'sexPredictB', headerName: "성별 분석 B", align: "center", width: 80},
        {
            field: 'qcA', headerName: "QC A", renderCell: (params) => (
                <div className={'QC__Cell__A'} style={{
                    backgroundColor: utils.setColor(params.value),
                    textAlign: "center",
                    margin: 0,
                    padding: 0
                }}>{utils.convertPassOrFail(params.value)}</div>
            ), width: 70
        },
        {
            field: 'qcB', headerName: "QC B", renderCell: (params) => (
                <div className={'QC__Cell__B'} style={{
                    backgroundColor: utils.setColor(params.value),
                    textAlign: "center",
                    margin: 0,
                    padding: 0
                }}>{utils.convertPassOrFail(params.value)}</div>
            ), width: 70
        },
        {
            field: 'analysisResult',
            headerName: "분석결과",
            editable: true,
            renderCell: (params) => (
                <div
                    style={{
                        backgroundColor: utils.setColor(params.value),
                        color: "white",
                        textAlign: "center",
                        alignContent: "center",
                        width: '100%',
                        height: '100%',
                        boxSizing: 'border-box',
                        fontWeight: 'bold'
                    }}
                >
                    {utils.convertResultName(params.value)}
                </div>
            ),
            renderEditCell: (params: GridRenderEditCellParams) => (
                <Select
                    value={params.value}
                    onChange={(event) => {
                        selectableColumnOnChange(params, event)
                    }}
                    fullWidth
                    sx={{
                        backgroundColor: utils.setColor(params.value),
                        color: 'white',
                    }}
                >
                    <MenuItem value="GENERAL">일반관리</MenuItem>
                    <MenuItem value="CONCERN">관심관리</MenuItem>
                    <MenuItem value="RISK">집중관리</MenuItem>
                </Select>
            ), width: 100
        },
        {field: 'requestDate', headerName: '의뢰일', align: 'center', width: 80},
        {field: 'requestTat', headerName: 'TAT', align: 'center', width: 80},
        {
            field: 'reportName', headerName: "결과지", type: "actions", cellClassName: 'reportsActions',
            getActions: ({id}) => {
                if (apiRef.current.getRow(id).reportName !== null && apiRef.current.getRow(id).reportName !== "")
                    return [
                        <GridActionsCellItem
                            icon={<SummarizeIcon/>}
                            label="report"
                            sx={{
                                color: 'primary.main',
                            }}
                            onClick={handleReportClick(id)}/>,
                    ]
                else return [
                    <GridActionsCellItem
                        icon={<SummarizeIcon/>}
                        label="report"
                        disabled={true}/>
                ]
            }, width: 60
        },
        {
            field: 'interpretation', headerName: "소견", type: "actions", cellClassName: 'interpretationActions',
            getActions: ({id}) => {
                if (apiRef.current.getRow(id).interpretation !== "")
                    return [<GridActionsCellItem
                        icon={<EditNoteIcon/>}
                        label="interpretation"
                        sx={{
                            color: 'primary.main'
                        }}
                        onClick={handleInterpretationClick(id)}/>]
                else return [
                    <GridActionsCellItem
                        icon={<EditNoteIcon/>}
                        label="interpretation"
                        sx={{
                            color: 'primary.secondary'
                        }}
                        onClick={handleInterpretationClick(id)}/>

                ]
            }, width: 60
        },
        {
            field: 'reportCreateAt',
            headerName: "생성시각",
            renderCell: (params) => (
                <div
                    className={'BASIC__CELL'}>{params.value === "null" ? "" : params.value.replace("T", " ").split(".")[0]}</div>),
            width: 120
        },
        {field: 'publishAt', headerName: "발송일", align: 'center', width: 90},
        {field: 'publisher', headerName: "발송자", align: 'center', width: 80},
        {
            field: 'too5Pred',
            headerName: "예측암종(남)",
            editable: true,
            renderCell: (params) => (
                <div
                    style={{
                        color: utils.setColor(params.value),
                        textAlign: "center",
                        alignContent: "center",
                        width: '100%',
                        height: '100%',
                        boxSizing: 'border-box',
                        fontWeight: 'bold'
                    }}
                >{utils.convertResultName(params.value)}</div>
            ),
            renderEditCell: (params: GridRenderEditCellParams) => (
                <Select
                    value={params.value}
                    onChange={(event) => {
                        selectableColumnOnChange(params, event)
                    }}
                    fullWidth
                    sx={{
                        color: utils.setColor(params.value)
                    }}
                >
                    <MenuItem value="LuC">폐암</MenuItem>
                    <MenuItem value="ESO">식도암</MenuItem>
                    <MenuItem value="colon">대장암</MenuItem>
                    <MenuItem value="Panc">췌장암</MenuItem>
                    <MenuItem value="HCC">간암</MenuItem>
                    <MenuItem value="Others">기타암</MenuItem>
                </Select>
            ), width: 90
        },
        {
            field: 'too5Fems', headerName: "too5\nFEMS prob", renderCell: (params) => (
                <div className={'RESULT__CELL'}>{params.value}</div>
            ), width: 150
        },
        {
            field: 'too6Pred',
            headerName: "예측암종(여)",
            editable: true,
            renderCell: (params) => (
                <div
                    style={{
                        color: utils.setColor(params.value),
                        textAlign: "center",
                        alignContent: "center",
                        width: '100%',
                        height: '100%',
                        boxSizing: 'border-box',
                        fontWeight: 'bold'
                    }}
                >{utils.convertResultName(params.value)}</div>
            ),
            renderEditCell: (params: GridRenderEditCellParams) => (
                <Select
                    value={params.value}
                    onChange={(event) => {
                        selectableColumnOnChange(params, event)
                    }}
                    fullWidth
                    sx={{
                        color: utils.setColor(params.value)
                    }}
                >
                    <MenuItem value="LuC">폐암</MenuItem>
                    <MenuItem value="ESO">식도암</MenuItem>
                    <MenuItem value="colon">대장암</MenuItem>
                    <MenuItem value="Panc">췌장암</MenuItem>
                    <MenuItem value="HCC">간암</MenuItem>
                    <MenuItem value="OV">난소암</MenuItem>
                    <MenuItem value="Others">기타암</MenuItem>
                </Select>
            ), width: 90
        },
        {
            field: 'too6Fems', headerName: "TOO6 FEMS PROB", renderCell: (params) => (
                <div className={'RESULT__CELL'}>{params.value}</div>
            ), align: "right", width: 150
        },
        {
            field: 'freemixA', headerName: "FREEMIX A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'rawReadMillionA', headerName: "RAW READ(Million) A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 150
        },
        {
            field: 'filteredReadsA', headerName: "FILTERED READS A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 150
        },
        {
            field: 'dupRateA', headerName: "DUP RATE A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 90
        },
        {
            field: 'gcA', headerName: "GC A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'meanA', headerName: "MEAN A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'medianA', headerName: "MEDIAN A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'chrXCountA', headerName: "chr X Count A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'chrXPropA', headerName: "chr X Proportion A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 120
        },
        {
            field: 'chrYCountA', headerName: "chr Y Count A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'chrYPropA', headerName: "chr Y Proportion A", renderCell: (params) => (
                <div className={'QC__Cell__A'}>{params.value}</div>
            ), width: 120
        },
        {
            field: 'freemixB', headerName: "FREEMIX B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'rawReadMillionB', headerName: "RAW READ(Million) B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 150
        },
        {
            field: 'filteredReadsB', headerName: "FILTERED READS B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'dupRateB', headerName: "DUP RATE B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'gcB', headerName: "GC B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'meanB', headerName: "MEAN B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'medianB', headerName: "MEDIAN B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 80
        },
        {
            field: 'chrXCountB', headerName: "chr X Count B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'chrXPropB', headerName: "chr X Proportion B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 120
        },
        {
            field: 'chrYCountB', headerName: "chr Y Count B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 100
        },
        {
            field: 'chrYPropB', headerName: "chr Y Proportion B", renderCell: (params) => (
                <div className={'QC__Cell__B'}>{params.value}</div>
            ), width: 120
        }
    ]
    const handleReportClick = (id: GridRowId) => () => {
        let reports = apiRef.current.getRow(id);

        (async () => {
            const report = await ReportPreviewAPI(reports["sampleId"], reports["serviceCode"], new Date(reports["reportCreateAt"]).getTime())
            const blob = new Blob([report], {type: 'application/pdf'});
            const url = URL.createObjectURL(blob);
            window.open(url);
        })()
    }
    const handleInterpretationClick = (id: GridRowId) => () => {
        let row = apiRef.current.getRow(id);
        setInterpretationDialog({
            open: true,
            sampleId: row.sampleId,
            serviceName: row.serviceName,
            serviceCode: row.serviceCode,
            batchName: row.batchName,
            rowNumber: row.rowNumber,
            patientName: row.patientName,
            interpretation: row.interpretation
        })
    }

    const handleRowSelection = (rowSelectionModel: GridRowId[]) => {
        setRowSelectionModel(rowSelectionModel);

        const selectedRowsData = rowSelectionModel.map((id) => {
            return apiRef.current.getRow(id);
        });
        setSelectedAnalysis(selectedRowsData)
    };

    return (
        <>
            <DataGrid
                columns={columns}
                rows={rows}
                className={"apper__animation"}

                disableClipboardPaste={false}
                columnHeaderHeight={40}
                checkboxSelection
                disableRowSelectionOnClick
                rowHeight={30}
                paginationModel={paginationModel}
                onPaginationModelChange={setPaginationModel}
                onRowSelectionModelChange={handleRowSelection}
                apiRef={apiRef}

                sx={{
                    'width': '100%',
                    '& .MuiDataGrid-cell': {
                        fontSize: '10px',
                        fontFamily: 'Montserrat, Noto Sans KR',
                        padding: '0 0'
                    },
                    '& .MuiDataGrid-columnHeaders': {
                        textAlign: 'center',
                        fontSize: '12px',

                        fontFamily: 'Montserrat, Noto Sans KR',
                    },
                    '& .MuiDataGrid-columnHeaderTitle': {
                        justifyContent: 'center',
                        display: 'flex',
                        width: '100%',
                        fontWeight: 'bold'
                    },
                    '& .MuiSvgIcon-root': { //전체 아이콘 크기 설정
                        fontSize: '16px',
                    },
                    '& .MuiDataGrid-virtualScroller': { //스크롤바 설정
                        '&::-webkit-scrollbar': {
                            width: '8px',
                        },
                        '&::-webkit-scrollbar-thumb': {
                            backgroundColor: '#888',
                            borderRadius: '10px',
                        },
                        '&::-webkit-scrollbar-thumb:hover': {
                            backgroundColor: '#555',
                        },
                        '&::-webkit-scrollbar-track': {
                            backgroundColor: '#f1f1f1',
                        },
                    }
                }}
            />
        </>
    )
}
