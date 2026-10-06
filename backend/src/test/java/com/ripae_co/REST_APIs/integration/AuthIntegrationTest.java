package com.ripae_co.REST_APIs.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.ripae_co.REST_APIs.dto.LoginRequest;
import com.ripae_co.REST_APIs.dto.LoginResponse;
import com.ripae_co.REST_APIs.dto.SignUpRequest;
import com.ripae_co.REST_APIs.entity.App_User;
import com.ripae_co.REST_APIs.repository.App_UserRepository;
import com.ripae_co.REST_APIs.service.AuthService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthIntegrationTest {

  @Autowired
  private AuthService authService;

  @Autowired
  private App_UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void testSignupAndLogin() {
    // Signup
    SignUpRequest signupReq = new SignUpRequest("johndoe", "john@example.com", "password123", "John Doe", "5551234567");
    authService.signup(signupReq);

    // Login
    LoginRequest loginReq = new LoginRequest("johndoe", "password123");
    LoginResponse response = authService.login(loginReq);

    assertNotNull(response);
    assertNotNull(response.getToken());
    assertEquals("johndoe", response.getUsername());
    assertEquals("john@example.com", response.getEmail());
  }

  @Test
  void testLoginInvalidCredentials() {
    SignUpRequest req = new SignUpRequest("testuser", "test@example.com", "password123", "Test User", null);
    authService.signup(req);

    LoginRequest loginReq = new LoginRequest("testuser", "wrongpassword");
    assertThrows(Exception.class, () -> authService.login(loginReq));
  }

  @Test
  void testRefreshToken() {
    // Signup and login
    SignUpRequest signupReq = new SignUpRequest("alice", "alice@example.com", "pass123", "Alice", null);
    authService.signup(signupReq);

    LoginRequest loginReq = new LoginRequest("alice", "pass123");
    LoginResponse loginResp = authService.login(loginReq);

    // Refresh token
    String authHeader = "Bearer " + loginResp.getToken();
    LoginResponse refreshResp = authService.refreshToken(authHeader);

    assertNotNull(refreshResp);
    assertNotNull(refreshResp.getToken());
    assertNotEquals(loginResp.getToken(), refreshResp.getToken());
  }
}
