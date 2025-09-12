import React, { useState } from 'react';
// NEW: นำเข้า useNavigate hook และ api service ของเรา
import { useNavigate } from 'react-router-dom';
import api from '../services/api'; // แก้ไข path ให้ถูกต้อง

function LoginPage() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const [error, setError] = useState('');
  const navigate = useNavigate(); // NEW: เรียกใช้ hook เพื่อให้เราสามารถสั่งเปลี่ยนหน้าได้

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      // NEW: เปลี่ยนจาก axios.post มาใช้ api.post แทน
      // เราไม่ต้องใส่ '/api' ข้างหน้าแล้ว เพราะกำหนดไว้ใน baseURL ของ instance แล้ว
      const response = await api.post('/auth/login', {
        username: formData.username,
        password: formData.password,
      });

      const { token } = response.data;
      localStorage.setItem('token', token);

      // NEW: นี่คือการสั่งให้เปลี่ยนหน้า!
      // navigate('/') จะพาผู้ใช้ไปยังหน้า Dashboard ทันที
      navigate('/');

    } catch (err) {
      console.error('Login failed:', err.response?.data || err.message);
      setError(err.response?.data || 'Login failed. Please try again.');
    }
  };

  // ... ส่วนของ JSX เหมือนเดิมทุกประการ ไม่ต้องแก้ไข ...
  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
        <h2 className="text-2xl font-bold text-center text-gray-900">
          Apartment Management
        </h2>
        <form className="space-y-6" onSubmit={handleSubmit}>
          {/* ... input fields ... */}
           <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium text-gray-700"
            >
              Username
            </label>
            <input
              id="username"
              name="username"
              type="text"
              required
              className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={formData.username}
              onChange={handleChange}
            />
          </div>
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium text-gray-700"
            >
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              required
              className="w-full px-3 py-2 mt-1 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              value={formData.password}
              onChange={handleChange}
            />
          </div>
          {error && (
            <div className="p-2 text-sm text-center text-red-800 bg-red-100 rounded-md">
              {error}
            </div>
          )}
          <div>
            <button
              type="submit"
              className="w-full px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Sign in
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;