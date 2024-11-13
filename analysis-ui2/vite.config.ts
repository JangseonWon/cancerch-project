import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => ({
  plugins: [react()],
  build: {
    rollupOptions: {
      input: {
        app: './analysis.html'
      }
    }
  },
  base: mode === 'production' ? "/avoid-service/analysis/" : "",
  server: {
    open: './analysis.html',
  }
}))
