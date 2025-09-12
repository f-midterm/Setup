package apartment.example.backend.repository;

import apartment.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // อย่าลืม import

public interface UserRepository extends JpaRepository<User, Long> {
    // Why: ตั้งชื่อเมธอดตาม Convention ของ Spring Data JPA (find by field name)
    // Spring จะเข้าใจและสร้าง query เพื่อค้นหาผู้ใช้จากคอลัมน์ username ให้เราโดยอัตโนมัติ
    Optional<User> findByUsername(String username);
}