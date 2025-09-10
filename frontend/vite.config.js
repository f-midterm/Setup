// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'

// // https://vitejs.dev/config/
// export default defineConfig({
//   plugins: [react()],
//   server: {
//     // This is crucial for Vite to work inside a Docker container.
//     // It tells Vite to listen on all available network interfaces,
//     // not just localhost.
//     host: true, 
//     port: 5173, // The port Vite will run on inside the container.
    
//     // This is needed for Hot Module Replacement (HMR) to work correctly
//     // when running behind a reverse proxy like Nginx.
//     hmr: {
//       clientPort: 80, // Or whatever port your Nginx is exposed on.
//     }
//   }
// })


import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // ทำให้ Vite รับการเชื่อมต่อจากภายนอก container ได้
    host: true, 
    port: 5173,
    
    // บังคับให้ Vite คอยเช็คไฟล์เองโดยตรง (สำคัญที่สุดสำหรับ Docker)
    watch: {
      usePolling: true
    }
  }
})
