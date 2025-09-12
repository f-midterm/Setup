package apartment.example.backend.entity;

import apartment.example.backend.entity.enums.UnitStatus; // NEW: import Enum
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "units")
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", unique = true, nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private int floor;

    // --- MODIFIED BLOCK ---
    // Why: เปลี่ยนจาก String เป็น UnitStatus เพื่อ Type Safety
    @Enumerated(EnumType.STRING) // NEW ANNOTATION
    @Column(nullable = false)
    private UnitStatus status; // MODIFIED
    // --- END MODIFIED BLOCK ---

    @Column(nullable = false)
    private String type;

    @Column(name = "rent_amount", nullable = false)
    private double rentAmount;

    @OneToOne(mappedBy = "unit")
    @JsonManagedReference
    private Tenant tenant;
}