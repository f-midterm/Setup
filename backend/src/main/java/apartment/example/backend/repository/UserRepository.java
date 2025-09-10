package apartment.example.backend.repository;

import apartment.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository บอก Spring ว่านี่คือส่วนที่ใช้จัดการข้อมูล
// JpaRepository<User, Long> เป็นเครื่องมือวิเศษของ Spring Data JPA
// มันจะสร้าง method พื้นฐานสำหรับจัดการข้อมูล (เช่น save, findById, findAll) ให้เราอัตโนมัติ
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // เราสามารถสร้าง method ของเราเองได้ แค่ตั้งชื่อตาม convention
    // Spring จะเข้าใจว่า method นี้ให้ "ค้นหา User จาก username"
    // Optional<User> หมายความว่าผลลัพธ์อาจจะเจอ (User) หรือไม่เจอ (empty) ก็ได้
    Optional<User> findByUsername(String username);
}
