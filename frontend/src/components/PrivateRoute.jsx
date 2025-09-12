import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

// Why: นี่คือ Component ที่ทำหน้าที่เป็น "ยาม"
// มันจะตรวจสอบว่าใน localStorage มี Token อยู่หรือไม่
const PrivateRoute = () => {
  const isAuthenticated = !!localStorage.getItem('token'); // Why: '!!' แปลงค่า string หรือ null ให้เป็น boolean (true/false)

  // Why: ถ้ามี Token (isAuthenticated เป็น true) ให้แสดง Component ลูก (Outlet)
  // แต่ถ้าไม่มี Token ให้ Redirect ไปที่หน้า /login ทันที
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};

export default PrivateRoute;