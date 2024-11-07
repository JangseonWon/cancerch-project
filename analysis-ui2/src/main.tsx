import React from 'react'
import ReactDOM from 'react-dom/client'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AnalysisPage from './analysis/AnalysisPage.tsx'
import './index.css'
const router = createBrowserRouter([
    {
        path: location.pathname,
        element: <AnalysisPage/>
    }
])
ReactDOM.createRoot(document.getElementById('root')!).render(
    <RouterProvider router={router}/>
)
