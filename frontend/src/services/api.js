import axios from 'axios';

// Why: สร้าง instance ของ axios ขึ้นมาใหม่โดยเฉพาะ
// การทำแบบนี้ช่วยให้เราสามารถตั้งค่าเฉพาะสำหรับ API ของเราได้
// โดยไม่กระทบกับ axios instance อื่นๆ ที่อาจมีในโปรเจกต์
const api = axios.create({
  // Why: กำหนด baseURL ให้ชี้ไปที่ prefix ของ API ของเรา
  // ซึ่ง Nginx จะรับช่วงต่อเพื่อส่งไปยัง Backend
  baseURL: '/api',
});

// Why: นี่คือหัวใจสำคัญ! Interceptor จะ "ดักจับ" ทุกๆ request ที่จะถูกส่งออกไป
// ก่อนที่ request จะถูกส่ง มันจะทำงานตามฟังก์ชันที่เราเขียนไว้
api.interceptors.request.use(
  (config) => {
    // Why: ดึง Token มาจาก localStorage
    const token = localStorage.getItem('token');
    if (token) {
      // Why: ถ้ามี Token ให้เพิ่ม Header 'Authorization' เข้าไปใน request
      // รูปแบบ 'Bearer ' + token เป็นมาตรฐานสากลของ OAuth2
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config; // Why: คืนค่า config ที่แก้ไขแล้วกลับไป เพื่อให้ request ถูกส่งต่อไปได้
  },
  (error) => {
    // Why: ถ้าเกิด Error ก่อนที่ request จะถูกส่ง ให้ reject Promise นั้น
    return Promise.reject(error);
  }
);

export default api;