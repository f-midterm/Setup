package apartment.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity บอก Spring ว่าคลาสนี้คือ "โมเดล" ที่ใช้เชื่อมกับตารางในฐานข้อมูล
@Entity
// @Table(name = "users") บอกให้เชื่อมกับตารางชื่อ "users" ที่เราสร้างไว้
@Table(name = "users")
public class User {

    // @Id และ @GeneratedValue บอกว่า field 'id' คือ Primary Key และให้ฐานข้อมูลสร้างค่าให้อัตโนมัติ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    // Getters and Setters
    // เป็น method มาตรฐานสำหรับให้ code ส่วนอื่นเข้ามาอ่านหรือแก้ไขค่าใน field ต่างๆ ได้
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
