package apartment.example.backend.controller;

import apartment.example.backend.dto.TenantRequestDto;
import apartment.example.backend.dto.TenantResponseDto;
import apartment.example.backend.entity.Tenant;
import apartment.example.backend.service.TenantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    // Why: สร้างเมธอดส่วนตัวสำหรับแปลง Entity เป็น DTO เพื่อลดการเขียนโค้ดซ้ำ
    private TenantResponseDto convertToDto(Tenant tenant) {
        TenantResponseDto dto = new TenantResponseDto();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setPhoneNumber(tenant.getPhoneNumber());
        dto.setEmail(tenant.getEmail());
        dto.setLeaseStartDate(tenant.getLeaseStartDate());
        dto.setLeaseEndDate(tenant.getLeaseEndDate());
        // Why: ดึงเลขห้องมาจาก Object Unit ที่ผูกกันอยู่
        if (tenant.getUnit() != null) {
            dto.setRoomNumber(tenant.getUnit().getRoomNumber());
        }
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<TenantResponseDto>> getAllTenants() {
        List<Tenant> tenants = tenantService.getAllTenants();
        // Why: แปลง List ของ Entity ให้เป็น List ของ DTO ก่อนส่งกลับไป
        List<TenantResponseDto> tenantDtos = tenants.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tenantDtos);
    }

    @PostMapping
    public ResponseEntity<TenantResponseDto> createTenant(@RequestBody TenantRequestDto tenantRequestDto) {
        Tenant createdTenant = tenantService.createTenant(tenantRequestDto);
        return new ResponseEntity<>(convertToDto(createdTenant), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponseDto> updateTenant(@PathVariable Long id, @RequestBody TenantRequestDto tenantRequestDto) {
        try {
            Tenant updatedTenant = tenantService.updateTenant(id, tenantRequestDto);
            return ResponseEntity.ok(convertToDto(updatedTenant));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null); // ส่ง 400 Bad Request ถ้าห้องไม่ว่าง
        }
    }

    // --- NEW DELETE ENDPOINT ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        try {
            tenantService.deleteTenant(id);
            return ResponseEntity.noContent().build(); // Why: noContent() (Status 204) เป็นวิธีมาตรฐานในการตอบกลับหลังลบสำเร็จ
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}