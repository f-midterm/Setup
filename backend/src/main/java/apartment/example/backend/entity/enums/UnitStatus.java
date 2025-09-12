package apartment.example.backend.entity.enums;

// Why: สร้าง Enum เพื่อกำหนดค่าสถานะที่เป็นไปได้ของห้องพัก
// ช่วยป้องกันการใส่ข้อมูลผิดและทำให้โค้ดจัดการได้ง่ายขึ้น
public enum UnitStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE
}