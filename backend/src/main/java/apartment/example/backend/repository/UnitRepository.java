package apartment.example.backend.repository;

import apartment.example.backend.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

// Why: JpaRepository มอบเมธอดพื้นฐานสำหรับ CRUD (save, findById, findAll, delete) ให้เราอัตโนมัติ
// โดยที่เราไม่ต้องเขียน SQL เองเลย
public interface UnitRepository extends JpaRepository<Unit, Long> {
}