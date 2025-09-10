package apartment.example.backend.dto;

// DTO (Data Transfer Object) เป็นคลาสธรรมดาที่ใช้เป็น "กล่อง"
// สำหรับรับ-ส่งข้อมูลระหว่าง Frontend กับ Backend
// ในที่นี้คือรับ username กับ password จากหน้าเว็บ
public class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
