import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// Layouts
import MainLayout from './layouts/MainLayout';

// Components
import PrivateRoute from './components/PrivateRoute';

// Pages
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import TenantPage from './pages/TenantPage';
import MaintenancePage from './pages/MaintenancePage';
import DocumentPage from './pages/DocumentPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        {/* Why: เปลี่ยนโครงสร้างให้ PrivateRoute ครอบ MainLayout */}
        {/* ทำให้ทุกๆหน้าที่อยู่ข้างใน (children) ต้อง Login ก่อน และจะมี Layout แบบเดียวกันทั้งหมด */}
        <Route path="/" element={<PrivateRoute />}>
          <Route element={<MainLayout />}>
            <Route index element={<DashboardPage />} />
            <Route path="tenants" element={<TenantPage />} />
            <Route path="maintenance" element={<MaintenancePage />} />
            <Route path="documents" element={<DocumentPage />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;