package apartment.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "lease_start_date", nullable = false)
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date", nullable = false)
    private LocalDate leaseEndDate;

    // --- RELATIONSHIP MAPPING ---
    // Why: @OneToOne บอกว่าผู้เช่า 1 คน จะอยู่ได้แค่ 1 ห้อง
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @JsonBackReference // Why: ป้องกันการเกิด Loop เวลาแปลงข้อมูลเป็น JSON
    private Unit unit;
}