import React from 'react';
// แก้ไข: เพิ่ม .jsx ต่อท้ายชื่อไฟล์เพื่อให้ Vite/React หาไฟล์เจอ
import LoginPage from './LoginPage.jsx'; 

// เราไม่ได้ใช้ App.css แล้ว เพราะใช้ Tailwind ใน LoginPage แทน จึงลบการ import นี้ออก
// import './App.css'; 

function App() {
  // ตอนนี้ให้ App แสดงผลแค่หน้า LoginPage
  // ลบ className="App" ออกได้ เพราะเราไม่ได้ใช้ CSS จาก App.css แล้ว
  return (
    <div>
      <LoginPage />
    </div>
  );
}

export default App;

