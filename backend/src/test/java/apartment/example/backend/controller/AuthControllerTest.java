// backend/src/test/java/apartment/example/backend/controller/AuthControllerTest.java
package apartment.example.backend.controller;

import apartment.example.backend.dto.LoginRequest;
import apartment.example.backend.dto.LoginResponse;
import apartment.example.backend.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Successful() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = "test-token";

        // Why: We mock the UserDetailsService to return a UserDetails object when called with the test username.
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        // Why: We mock the JwtUtil to return a token when it's asked to generate one.
        when(jwtUtil.generateToken(userDetails)).thenReturn(token);

        // When
        ResponseEntity<?> responseEntity = authController.login(loginRequest);

        // Then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(token, ((LoginResponse) responseEntity.getBody()).getToken());
    }

    @Test
    void login_Failed_BadCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrong-password");

        // Why: We configure the AuthenticationManager to throw a BadCredentialsException to simulate a failed login.
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Incorrect username or password"));

        // When
        ResponseEntity<?> responseEntity = authController.login(loginRequest);

        // Then
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Incorrect username or password", responseEntity.getBody());
    }
}