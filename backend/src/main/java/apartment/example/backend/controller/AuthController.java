package apartment.example.backend.controller;

import apartment.example.backend.dto.LoginRequest;
import apartment.example.backend.dto.LoginResponse;
import apartment.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Why: ฉีด Beans ที่จำเป็นสำหรับการทำงานเข้ามา
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // Why: สร้าง Endpoint สำหรับการ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Why: ใช้ AuthenticationManager เพื่อตรวจสอบ username/password ที่ส่งมา
            // ถ้าไม่ถูกต้อง ส่วนนี้จะโยน Exception ออกมาทันที
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Why: ถ้าการยืนยันตัวตนล้มเหลว ให้ส่ง HTTP Status 401 Unauthorized กลับไป
            // ซึ่งเป็นวิธีที่ถูกต้องในการแจ้งว่าข้อมูล Login ไม่ถูกต้อง
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        // Why: ถ้า authenticate ผ่าน ให้โหลดข้อมูล UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        // Why: สร้าง JWT Token จาก UserDetails
        final String token = jwtUtil.generateToken(userDetails);

        // Why: ส่ง Token กลับไปให้ Client พร้อมกับ HTTP Status 200 OK
        return ResponseEntity.ok(new LoginResponse(token));
    }
}