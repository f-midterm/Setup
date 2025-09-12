import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';

const Sidebar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  // Why: สร้าง Array ของ Link เพื่อให้ง่ายต่อการจัดการและเพิ่มเมนูในอนาคต
  const navLinks = [
    { to: "/", text: "แดชบอร์ด" },
    { to: "/tenants", text: "จัดการผู้เช่า" },
    { to: "/maintenance", text: "จัดการซ่อมบำรุง" },
    { to: "/documents", text: "สร้างเอกสาร" },
  ];

  return (
    <aside className="w-64 bg-white shadow-lg flex-shrink-0">
      <div className="p-6 border-b">
        <h1 className="text-2xl font-bold text-indigo-600">Apartment Admin</h1>
      </div>
      <nav className="mt-4 px-4 space-y-2">
        {navLinks.map((link) => (
          // Why: NavLink เป็น Component ของ React Router ที่จะเพิ่ม class 'active' ให้เอง
          // เมื่อ URL ตรงกับ 'to' prop ทำให้เราสามารถสไตล์ลิงก์ที่ถูกเลือกได้ง่าย
          <NavLink
            key={link.to}
            to={link.to}
            end // 'end' prop ทำให้ลิงก์ "/" ไม่ active ตลอดเวลา
            className={({ isActive }) =>
              `block px-4 py-2 rounded-md font-medium transition-colors ${
                isActive ? 'bg-indigo-100 text-indigo-700' : 'text-slate-600 hover:bg-slate-100'
              }`
            }
          >
            {link.text}
          </NavLink>
        ))}
      </nav>
      <div className="absolute bottom-0 w-full p-4">
        <button
          onClick={handleLogout}
          className="w-52 px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700"
        >
          Logout
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;