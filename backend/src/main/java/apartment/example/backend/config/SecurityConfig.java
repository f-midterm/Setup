package apartment.example.backend.config;

import apartment.example.backend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Collections;

// This class configures the security settings for the application.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Creates a password encoder to hash passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Provides the standard authentication manager.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Defines how to find a user by their email for Spring Security.
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            return userRepository.findByEmail(email)
                    .map(user -> new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority(user.getLevel().name()))
                    ))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        };
    }

    // Configures the security rules for HTTP requests.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(authz -> authz
                        // Allow anyone to access login and register endpoints.
                        .requestMatchers("/api/auth/**").permitAll()
                        // Any other request requires authentication.
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}