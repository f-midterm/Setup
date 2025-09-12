package apartment.example.backend.dto;

import lombok.Data;
import java.time.LocalDate;

// Why: DTO สำหรับส่งข้อมูล Tenant กลับไปให้ Frontend
// เราเลือกเฉพาะ field ที่จำเป็นต้องแสดงผล และเพิ่ม field 'roomNumber' เข้ามาเพื่อความสะดวก
@Data
public class TenantResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    private String roomNumber; // Why: เพิ่ม field นี้เพื่อให้ Frontend แสดงผลเลขห้องได้ทันที
}