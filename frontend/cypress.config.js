// frontend/cypress.config.js
import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost', // ใช้ Port 80 ที่เป็นของ Nginx
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});