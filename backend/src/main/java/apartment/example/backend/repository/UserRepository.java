package apartment.example.backend.repository;

import apartment.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // เราจะเปลี่ยนจากการค้นหาด้วย username มาเป็นการค้นหาด้วย email
    // เพราะ email ควรจะเป็นค่าที่ไม่ซ้ำกันสำหรับผู้ใช้แต่ละคน
    Optional<User> findByEmail(String email);
}

