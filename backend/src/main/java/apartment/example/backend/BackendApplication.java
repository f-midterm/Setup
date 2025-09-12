package apartment.example.backend;

import apartment.example.backend.entity.Unit;
import apartment.example.backend.entity.User;
import apartment.example.backend.repository.UnitRepository; // NEW
import apartment.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import apartment.example.backend.entity.enums.UnitStatus;
import java.util.List; // NEW
import java.util.stream.IntStream; // NEW

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // Why: แก้ไข Bean นี้ให้รับ UnitRepository เพิ่มเข้ามา
    // และเพิ่ม Logic ในการสร้างข้อมูลห้องพักเริ่มต้น
    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UnitRepository unitRepository) { // NEW: Inject UnitRepository

        return args -> {
            // --- Create default admin user ---
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setEmail("admin@apartment.com");
                userRepository.save(admin);
                System.out.println(">>> Created default admin user with password 'password'");
            }

            // --- Create default units --- NEW BLOCK
            // Why: ตรวจสอบก่อนว่ามีห้องพักในระบบแล้วหรือยัง ถ้ายังไม่มีเลยถึงจะสร้างใหม่
            if (unitRepository.count() == 0) {
                System.out.println(">>> Creating initial units...");
                IntStream.rangeClosed(1, 2).forEach(floor -> {
                    IntStream.rangeClosed(1, 12).forEach(roomNum -> {
                        Unit unit = new Unit();
                        unit.setFloor(floor);
                        unit.setRoomNumber(String.format("%d%02d", floor, roomNum));
                        unit.setStatus(UnitStatus.AVAILABLE); // MODIFIED
                        unit.setType("STUDIO");
                        unit.setRentAmount(4500.00);
                        unitRepository.save(unit);
                    });
                });
                System.out.println(">>> Finished creating 24 units.");
            }
        };
    }
}