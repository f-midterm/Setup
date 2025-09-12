package apartment.example.backend.service;

import apartment.example.backend.dto.TenantRequestDto;
import apartment.example.backend.entity.Tenant;
import apartment.example.backend.entity.Unit;
import apartment.example.backend.entity.enums.UnitStatus;
import apartment.example.backend.repository.TenantRepository;
import apartment.example.backend.repository.UnitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UnitRepository unitRepository;

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    // Why: @Transactional ทำให้มั่นใจว่าถ้ามีขั้นตอนไหนล้มเหลว
    // การเปลี่ยนแปลงทั้งหมด (ทั้งในตาราง tenants และ units) จะถูกยกเลิก (Rollback)
    // เพื่อป้องกันข้อมูลที่ไม่สมบูรณ์
    @Transactional
    public Tenant createTenant(TenantRequestDto tenantRequestDto) {
        // 1. ค้นหาห้องจาก ID ที่ส่งมา
        Unit unit = unitRepository.findById(tenantRequestDto.getUnitId())
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + tenantRequestDto.getUnitId()));

        // 2. ตรวจสอบว่าห้องว่างหรือไม่ (Business Logic ที่สำคัญ)
        if (unit.getStatus() != UnitStatus.AVAILABLE) {
            throw new IllegalStateException("Unit " + unit.getRoomNumber() + " is not available.");
        }

        // 3. สร้าง Object Tenant ใหม่
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequestDto.getName());
        tenant.setPhoneNumber(tenantRequestDto.getPhoneNumber());
        tenant.setEmail(tenantRequestDto.getEmail());
        tenant.setLeaseStartDate(tenantRequestDto.getLeaseStartDate());
        tenant.setLeaseEndDate(tenantRequestDto.getLeaseEndDate());
        tenant.setUnit(unit);

        // 4. อัปเดตสถานะห้องเป็น OCCUPIED
        unit.setStatus(UnitStatus.OCCUPIED);
        unitRepository.save(unit);

        // 5. บันทึกข้อมูลผู้เช่าคนใหม่
        return tenantRepository.save(tenant);
    }
    @Transactional
    public Tenant updateTenant(Long id, TenantRequestDto tenantRequestDto) {
        // 1. ค้นหาผู้เช่าเดิม
        Tenant existingTenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found with id: " + id));

        // 2. อัปเดตข้อมูลพื้นฐาน
        existingTenant.setName(tenantRequestDto.getName());
        existingTenant.setPhoneNumber(tenantRequestDto.getPhoneNumber());
        existingTenant.setEmail(tenantRequestDto.getEmail());
        existingTenant.setLeaseStartDate(tenantRequestDto.getLeaseStartDate());
        existingTenant.setLeaseEndDate(tenantRequestDto.getLeaseEndDate());

        // 3. จัดการเรื่องการย้ายห้อง (Logic ที่ซับซ้อน)
        Unit currentUnit = existingTenant.getUnit();
        Long newUnitId = tenantRequestDto.getUnitId();

        if (!currentUnit.getId().equals(newUnitId)) {
            // ค้นหาห้องใหม่
            Unit newUnit = unitRepository.findById(newUnitId)
                    .orElseThrow(() -> new EntityNotFoundException("New unit not found with id: " + newUnitId));

            // ตรวจสอบว่าห้องใหม่ว่างหรือไม่
            if (newUnit.getStatus() != UnitStatus.AVAILABLE) {
                throw new IllegalStateException("New unit " + newUnit.getRoomNumber() + " is not available.");
            }

            // ทำให้ห้องเก่ากลับมาว่าง
            currentUnit.setStatus(UnitStatus.AVAILABLE);
            unitRepository.save(currentUnit);

            // ย้ายผู้เช่าไปห้องใหม่และทำให้ห้องใหม่ไม่ว่าง
            existingTenant.setUnit(newUnit);
            newUnit.setStatus(UnitStatus.OCCUPIED);
            unitRepository.save(newUnit);
        }

        // 4. บันทึกข้อมูลผู้เช่าที่อัปเดตแล้ว
        return tenantRepository.save(existingTenant);
    }

    // --- NEW DELETE METHOD ---
    @Transactional
    public void deleteTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tenant not found with id: " + id));

        // ทำให้ห้องที่เคยอยู่กลับมาว่าง
        Unit unit = tenant.getUnit();
        if (unit != null) {
            unit.setStatus(UnitStatus.AVAILABLE);
            unitRepository.save(unit);
        }

        // ลบข้อมูลผู้เช่า
        tenantRepository.delete(tenant);
    }
}