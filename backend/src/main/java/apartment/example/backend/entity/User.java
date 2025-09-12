package apartment.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

// Why: @Table(name = "users") เป็นการระบุชื่อตารางในฐานข้อมูลให้ชัดเจน
// เพื่อหลีกเลี่ยงปัญหาชื่อ "user" ซึ่งอาจเป็นคำสงวนใน SQL บางเวอร์ชัน
@Entity
@Table(name = "users")
@Data
// Why: ทำให้คลาส User ของเราเข้ากันได้กับ Spring Security
// โดย implement interface UserDetails ซึ่งบังคับให้เราต้องมี method บางอย่าง
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    // Why: เมธอดนี้ใช้สำหรับระบุ "สิทธิ์" หรือ "Role" ของผู้ใช้
    // ในโปรเจกต์นี้เรามีแค่ Admin ดังนั้นเราจึง return ค่าว่างไปก่อนได้
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    // Why: เมธอดเหล่านี้ใช้ตรวจสอบสถานะของบัญชีผู้ใช้ เช่น บัญชีหมดอายุ, ถูกล็อค
    // สำหรับตอนนี้เราตั้งให้เป็น true ทั้งหมด เพื่อให้ใช้งานได้เสมอ
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}