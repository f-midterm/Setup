package apartment.example.backend.service;

import apartment.example.backend.entity.Unit;
import apartment.example.backend.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Why: แยก Business Logic ออกมาไว้ที่ Service Layer ทำให้ Controller มีหน้าที่แค่รับส่งข้อมูล
// และโค้ดเป็นระเบียบ ง่ายต่อการทดสอบและบำรุงรักษา
@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}