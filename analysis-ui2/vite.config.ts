import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      input: {
        app: './analysis.html'
      }
    }
  },
  base: "analysis/",
  server: {
    open: 'analysis/analysis.html',
  }
})
