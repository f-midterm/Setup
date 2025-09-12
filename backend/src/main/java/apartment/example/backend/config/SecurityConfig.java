package apartment.example.backend.config;

import apartment.example.backend.security.JwtAuthenticationFilter; // NEW!
import org.springframework.beans.factory.annotation.Autowired; // NEW!
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // NEW!

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NEW! Why: ฉีด JwtAuthenticationFilter ที่เราสร้างไว้เข้ามา เพื่อนำไปใช้งาน
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // NEW! Why: เพิ่ม Filter ของเราเข้าไปใน Security Chain
                // โดยให้ทำงาน "ก่อน" Filter มาตรฐานที่ชื่อ UsernamePasswordAuthenticationFilter
                // เพื่อให้เราสามารถตรวจสอบ JWT ได้ก่อนที่ Spring จะพยายามตรวจสอบด้วย username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}