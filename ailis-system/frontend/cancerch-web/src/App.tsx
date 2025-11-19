import { Routes, Route, Link } from 'react-router-dom'
import { WorklistList } from './pages/WorklistList'
import { WorklistDetail } from './pages/WorklistDetail'
import { CreateWorklist } from './pages/CreateWorklist'
import { PreprocessingList } from './pages/PreprocessingList'
import { SequencingList } from './pages/SequencingList'
import { AnalysisList } from './pages/AnalysisList'
import { ReportList } from './pages/ReportList'

function App() {
  return (
    <div className="app-container">
      <header className="app-header">
        <h1>
          <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>
            AILIS - Cancer Check System
          </Link>
        </h1>
      </header>

      <main className="app-content">
        <Routes>
          <Route path="/" element={<WorklistList />} />
          <Route path="/worklists/new" element={<CreateWorklist />} />
          <Route path="/worklists/:id" element={<WorklistDetail />} />
          <Route path="/preprocessing" element={<PreprocessingList />} />
          <Route path="/sequencing" element={<SequencingList />} />
          <Route path="/analysis" element={<AnalysisList />} />
          <Route path="/reports" element={<ReportList />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
