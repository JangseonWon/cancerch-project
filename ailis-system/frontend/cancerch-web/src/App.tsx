import { Routes, Route, Link } from 'react-router-dom'
import { WorklistList } from './pages/WorklistList'
import { WorklistDetail } from './pages/WorklistDetail'
import { CreateWorklist } from './pages/CreateWorklist'

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
        </Routes>
      </main>
    </div>
  )
}

export default App
