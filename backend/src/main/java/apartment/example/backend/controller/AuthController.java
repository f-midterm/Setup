package apartment.example.backend.controller;

import apartment.example.backend.dto.LoginRequest;
import apartment.example.backend.entity.User;
import apartment.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// @RestController บอก Spring ว่าคลาสนี้ทำหน้าที่เป็น API Controller
@RestController
// @RequestMapping("/api/auth") กำหนด path หลักสำหรับ API ทั้งหมดในคลาสนี้
@RequestMapping("/api/auth")
public class AuthController {

    // @Autowired เป็นการ "ฉีด" หรือใส่ UserRepository ที่ Spring สร้างไว้ให้เข้ามาใน Controller
    // เพื่อให้เราสามารถเรียกใช้ method ต่างๆ ของมันได้
    @Autowired
    private UserRepository userRepository;

    // @PostMapping("/login") บอกว่า method นี้จะทำงานเมื่อมี HTTP POST request มาที่ /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // @RequestBody บอกให้ Spring แปลง JSON ที่ส่งมาจาก Frontend มาเป็น object LoginRequest

        // ค้นหา user จาก username ที่ส่งมา
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // ตรวจสอบว่าเจอ user ไหม และรหัสผ่านตรงกันหรือเปล่า
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                // ถ้าทุกอย่างถูกต้อง, ส่งข้อความ "Login successful" กลับไปพร้อม status 200 OK
                return ResponseEntity.ok("Login successful");
            }
        }

        // ถ้าไม่เจอ user หรือรหัสผ่านผิด, ส่งข้อความ "Invalid username or password" กลับไปพร้อม status 401 Unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
