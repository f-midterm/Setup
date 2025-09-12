package apartment.example.backend.controller;

import apartment.example.backend.entity.Unit;
import apartment.example.backend.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/units") // Why: กำหนด Path หลักสำหรับ API ที่เกี่ยวกับห้องพักทั้งหมด
public class UnitController {

    @Autowired
    private UnitService unitService;

    // Why: สร้าง GET Endpoint ที่ /api/units เพื่อดึงข้อมูลห้องพักทั้งหมด
    // ResponseEntity.ok(...) จะส่งข้อมูลกลับไปพร้อมกับ HTTP Status 200 OK
    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnits() {
        List<Unit> units = unitService.getAllUnits();
        return ResponseEntity.ok(units);
    }
}