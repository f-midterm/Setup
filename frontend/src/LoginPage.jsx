import React, { useState } from 'react';

function LoginPage() {
    // useState ใช้สำหรับเก็บค่าของ input และข้อความที่จะแสดงผล
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    // ฟังก์ชันนี้จะทำงานเมื่อกดปุ่ม Login
    const handleSubmit = async (event) => {
        event.preventDefault(); // ป้องกันไม่ให้หน้าเว็บ refresh
        setMessage('Logging in...');

        try {
            // ส่ง request ไปที่ Backend ผ่าน Nginx
            // เราใช้ /api/auth/login เพราะ Nginx จะส่งต่อไปที่ backend:8080 ให้เอง
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            // อ่านข้อความที่ Backend ตอบกลับมา
            const responseText = await response.text();

            if (response.ok) {
                // ถ้า response status เป็น 2xx (เช่น 200 OK)
                setMessage(`Success: ${responseText}`);
            } else {
                // ถ้า response status เป็น 4xx หรือ 5xx (เช่น 401 Unauthorized)
                setMessage(`Error: ${responseText}`);
            }
        } catch (error) {
            // กรณีที่ network error หรือ Nginx/Backend ไม่ทำงาน
            setMessage('Failed to connect to the server.');
            console.error('Login error:', error);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
                <div className="text-center">
                    <h2 className="text-3xl font-extrabold text-gray-900">
                        Admin Dashboard Login
                    </h2>
                    <p className="mt-2 text-sm text-gray-600">
                        Sign in to your account
                    </p>
                </div>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div className="space-y-4 rounded-md shadow-sm">
                        <div>
                            <label htmlFor="username" className="sr-only">Username</label>
                            <input
                                id="username"
                                name="username"
                                type="text"
                                required
                                className="relative block w-full px-3 py-3 text-gray-900 placeholder-gray-500 border border-gray-300 rounded-md appearance-none focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Username"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div>
                            <label htmlFor="password" className="sr-only">Password</label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                required
                                className="relative block w-full px-3 py-3 text-gray-900 placeholder-gray-500 border border-gray-300 rounded-md appearance-none focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                    </div>

                    <div>
                        <button
                            type="submit"
                            className="relative flex justify-center w-full px-4 py-3 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md group hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                            Sign in
                        </button>
                    </div>
                </form>
                {/* ส่วนแสดงข้อความผลลัพธ์ */}
                {message && (
                    <div className="p-4 mt-4 text-center text-sm text-gray-700 bg-gray-100 rounded-lg">
                        {message}
                    </div>
                )}
            </div>
        </div>
    );
}

export default LoginPage;
