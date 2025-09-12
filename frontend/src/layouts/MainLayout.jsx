import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/Sidebar';

// Why: Component นี้ทำหน้าที่เป็น "โครงบ้าน" สำหรับทุกหน้าที่ต้อง Login เข้ามา
// ประกอบด้วย Sidebar และส่วนแสดงผลหลัก (Outlet)
const MainLayout = () => {
  return (
    <div className="flex h-screen bg-slate-100">
      <Sidebar />
      <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-y-auto">
        {/* Why: <Outlet /> คือตำแหน่งที่ React Router จะนำ Component ลูก (เช่น DashboardPage) มาแสดงผล */}
        <Outlet />
      </main>
    </div>
  );
};

export default MainLayout;