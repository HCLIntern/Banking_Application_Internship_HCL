package com.digitalbanking.service;

import com.digitalbanking.dto.LoginRequest;
import com.digitalbanking.dto.LoginResponse;
import com.digitalbanking.dto.SignUpRequest;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.UnauthorizedException;
import com.digitalbanking.repository.UserRepository;
import com.digitalbanking.security.JwtTokenProvider;
import com.digitalbanking.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private AuthServiceImpl authService;

  @BeforeEach
  void setUp() {
    authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtTokenProvider);
  }

  // ========== LOGIN TESTS ==========

  @Test
  void testLoginSuccess() {
    // Arrange
    LoginRequest request = new LoginRequest("john_doe", "password123");
    User user = User.builder()
        .id(1L)
        .username("john_doe")
        .email("john@example.com")
        .password("$2a$10$hashedPassword")
        .fullName("John Doe")
        .role(User.UserRole.CUSTOMER)
        .isActive(true)
        .build();

    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password123", "$2a$10$hashedPassword")).thenReturn(true);
    when(jwtTokenProvider.generateToken(1L, "john_doe")).thenReturn("jwt-token-here");

    // Act
    LoginResponse response = authService.login(request);

    // Assert
    assertNotNull(response);
    assertEquals("jwt-token-here", response.getToken());
    assertEquals("john_doe", response.getUsername());
    assertEquals("CUSTOMER", response.getRole());
    assertEquals(1L, response.getId());

    // Verify
    verify(userRepository, times(1)).findByUsername("john_doe");
    verify(passwordEncoder, times(1)).matches("password123", "$2a$10$hashedPassword");
    verify(jwtTokenProvider, times(1)).generateToken(1L, "john_doe");
  }

  @Test
  void testLoginUserNotFound() {
    // Arrange
    LoginRequest request = new LoginRequest("nonexistent", "password123");
    when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(UnauthorizedException.class, () -> authService.login(request));
    verify(userRepository, times(1)).findByUsername("nonexistent");
  }

  @Test
  void testLoginUserAccountDisabled() {
    // Arrange
    LoginRequest request = new LoginRequest("john_doe", "password123");
    User user = User.builder()
        .id(1L)
        .username("john_doe")
        .isActive(false)
        .build();

    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

    // Act & Assert
    assertThrows(UnauthorizedException.class, () -> authService.login(request));
  }

  @Test
  void testLoginInvalidPassword() {
    // Arrange
    LoginRequest request = new LoginRequest("john_doe", "wrongpassword");
    User user = User.builder()
        .id(1L)
        .username("john_doe")
        .password("$2a$10$hashedPassword")
        .isActive(true)
        .build();

    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongpassword", "$2a$10$hashedPassword")).thenReturn(false);

    // Act & Assert
    assertThrows(UnauthorizedException.class, () -> authService.login(request));
  }

  // ========== SIGNUP TESTS ==========

  @Test
  void testSignupSuccess() {
    // Arrange
    SignUpRequest request = new SignUpRequest("jane_smith", "jane@example.com", "password123", "Jane Smith", "+1234567890");
    
    when(userRepository.existsByUsername("jane_smith")).thenReturn(false);
    when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
    when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedPassword");
    when(userRepository.save(any(User.class))).thenReturn(new User());

    // Act
    authService.signup(request);

    // Assert
    verify(userRepository, times(1)).existsByUsername("jane_smith");
    verify(userRepository, times(1)).existsByEmail("jane@example.com");
    verify(passwordEncoder, times(1)).encode("password123");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testSignupEmptyUsername() {
    // Arrange
    SignUpRequest request = new SignUpRequest("", "jane@example.com", "password123", "Jane Smith", "+1234567890");

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
  }

  @Test
  void testSignupDuplicateUsername() {
    // Arrange
    SignUpRequest request = new SignUpRequest("existing_user", "jane@example.com", "password123", "Jane Smith", "+1234567890");
    when(userRepository.existsByUsername("existing_user")).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
  }

  @Test
  void testSignupDuplicateEmail() {
    // Arrange
    SignUpRequest request = new SignUpRequest("jane_smith", "existing@example.com", "password123", "Jane Smith", "+1234567890");
    
    when(userRepository.existsByUsername("jane_smith")).thenReturn(false);
    when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
  }

  @Test
  void testSignupShortPassword() {
    // Arrange
    SignUpRequest request = new SignUpRequest("jane_smith", "jane@example.com", "12345", "Jane Smith", "+1234567890");

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
  }

  @Test
  void testSignupEmptyFullName() {
    // Arrange
    SignUpRequest request = new SignUpRequest("jane_smith", "jane@example.com", "password123", "", "+1234567890");

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> authService.signup(request));
  }

  // ========== REFRESH TOKEN TESTS ==========

  @Test
  void testRefreshTokenSuccess() {
    // Arrange
    String token = "Bearer eyJhbGciOiJIUzUxMiJ9...";
    String cleanToken = "eyJhbGciOiJIUzUxMiJ9...";

    User user = User.builder()
        .id(1L)
        .username("john_doe")
        .email("john@example.com")
        .fullName("John Doe")
        .role(User.UserRole.CUSTOMER)
        .isActive(true)
        .build();

    when(jwtTokenProvider.isTokenValid(cleanToken)).thenReturn(true);
    when(jwtTokenProvider.getUsernameFromToken(cleanToken)).thenReturn("john_doe");
    when(jwtTokenProvider.getUserIdFromToken(cleanToken)).thenReturn(1L);
    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
    when(jwtTokenProvider.generateToken(1L, "john_doe")).thenReturn("new-jwt-token");

    // Act
    LoginResponse response = authService.refreshToken(token);

    // Assert
    assertNotNull(response);
    assertEquals("new-jwt-token", response.getToken());
    assertEquals("john_doe", response.getUsername());

    // Verify
    verify(jwtTokenProvider, times(1)).isTokenValid(cleanToken);
    verify(jwtTokenProvider, times(1)).generateToken(1L, "john_doe");
  }

  @Test
  void testRefreshTokenInvalid() {
    // Arrange
    String token = "Bearer invalid-token";
    when(jwtTokenProvider.isTokenValid("invalid-token")).thenReturn(false);

    // Act & Assert
    assertThrows(UnauthorizedException.class, () -> authService.refreshToken(token));
  }

  @Test
  void testRefreshTokenUserDisabled() {
    // Arrange
    String token = "Bearer eyJhbGciOiJIUzUxMiJ9...";
    String cleanToken = "eyJhbGciOiJIUzUxMiJ9...";

    User user = User.builder()
        .id(1L)
        .username("john_doe")
        .isActive(false)
        .build();

    when(jwtTokenProvider.isTokenValid(cleanToken)).thenReturn(true);
    when(jwtTokenProvider.getUsernameFromToken(cleanToken)).thenReturn("john_doe");
    when(jwtTokenProvider.getUserIdFromToken(cleanToken)).thenReturn(1L);
    when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

    // Act & Assert
    assertThrows(UnauthorizedException.class, () -> authService.refreshToken(token));
  }
}
