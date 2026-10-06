package com.digitalbanking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ripae_co.REST_APIs.dto.LoginRequest;
import com.ripae_co.REST_APIs.dto.LoginResponse;
import com.ripae_co.REST_APIs.dto.SignUpRequest;
import com.ripae_co.REST_APIs.entity.User;
import com.ripae_co.REST_APIs.exception.ResourceNotFoundException;
import com.ripae_co.REST_APIs.exception.UnauthorizedException;
import com.ripae_co.REST_APIs.repository.UserRepository;
import com.ripae_co.REST_APIs.security.JwtTokenProvider;
import com.ripae_co.REST_APIs.service.AuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  /**
   * Authenticates user with username and password.
   * Validates credentials and returns JWT token if valid.
   *
   * @param request LoginRequest containing username and password
   * @return LoginResponse with JWT token and user details
   * @throws UnauthorizedException if credentials are invalid
   */
  @Override
  public LoginResponse login(LoginRequest request) {
    log.info("Login attempt for username: {}", request.getUsername());

    // Step 1: Find user by username
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> {
          log.warn("Login failed: User not found with username {}", request.getUsername());
          return new UnauthorizedException("Invalid username or password");
        });

    // Step 2: Check if user is active
    if (!user.isActive()) {
      log.warn("Login failed: User account is disabled - {}", request.getUsername());
      throw new UnauthorizedException("User account is disabled. Contact support.");
    }

    // Step 3: Verify password (compare plaintext with BCrypt hash)
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      log.warn("Login failed: Invalid password for username {}", request.getUsername());
      throw new UnauthorizedException("Invalid username or password");
    }

    // Step 4: Generate JWT token
    String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
    log.info("Login successful for user: {}", request.getUsername());

    // Step 5: Build and return response with token + user info
    return new LoginResponse(
        token,
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getFullName(),
        user.getRole().toString()
    );
  }

  /**
   * Registers a new user with validation.
   * Checks for duplicate username/email, hashes password, and saves to database.
   *
   * @param request SignUpRequest containing user registration details
   * @throws IllegalArgumentException if username/email already exists or validation fails
   */
  @Override
  public void signup(SignUpRequest request) {
    log.info("Signup attempt for username: {}", request.getUsername());

    // Step 1: Validate input fields
    if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("Username cannot be empty");
    }
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if (request.getPassword() == null || request.getPassword().length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters");
    }
    if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
      throw new IllegalArgumentException("Full name cannot be empty");
    }

    // Step 2: Check if username already exists
    if (userRepository.existsByUsername(request.getUsername())) {
      log.warn("Signup failed: Username already exists - {}", request.getUsername());
      throw new IllegalArgumentException("Username already taken. Please choose another.");
    }

    // Step 3: Check if email already exists
    if (userRepository.existsByEmail(request.getEmail())) {
      log.warn("Signup failed: Email already exists - {}", request.getEmail());
      throw new IllegalArgumentException("Email already registered. Please use another.");
    }

    // Step 4: Create new user entity
    User newUser = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .fullName(request.getFullName())
        .phone(request.getPhone())
        // Hash password using BCrypt (never store plain text!)
        .password(passwordEncoder.encode(request.getPassword()))
        // New users are customers by default
        .role(User.UserRole.CUSTOMER)
        // User is active by default
        .isActive(true)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    // Step 5: Save user to database
    userRepository.save(newUser);
    log.info("Signup successful for username: {}", request.getUsername());
  }

  /**
   * Refreshes JWT token for an authenticated user.
   * Validates existing token and generates a new one with extended expiration.
   *
   * @param token JWT token from Authorization header (with "Bearer " prefix)
   * @return LoginResponse with new JWT token
   * @throws UnauthorizedException if token is invalid or expired
   */
  @Override
  public LoginResponse refreshToken(String token) {
    log.info("Token refresh attempt");

    // Step 1: Remove "Bearer " prefix if present
    if (token.startsWith("Bearer ")) {
      token = token.substring(7);
    }

    // Step 2: Validate token signature and expiration
    if (!jwtTokenProvider.isTokenValid(token)) {
      log.warn("Token refresh failed: Invalid or expired token");
      throw new UnauthorizedException("Invalid or expired token. Please login again.");
    }

    // Step 3: Extract username from token
    String username = jwtTokenProvider.getUsernameFromToken(token);
    Long userId = jwtTokenProvider.getUserIdFromToken(token);

    // Step 4: Find user in database
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.warn("Token refresh failed: User not found - {}", username);
          return new ResourceNotFoundException("User not found");
        });

    // Step 5: Check if user is still active
    if (!user.isActive()) {
      log.warn("Token refresh failed: User account disabled - {}", username);
      throw new UnauthorizedException("User account is disabled");
    }

    // Step 6: Generate new token with fresh expiration
    String newToken = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
    log.info("Token refreshed successfully for user: {}", username);

    // Step 7: Return new token with user info
    return new LoginResponse(
        newToken,
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getFullName(),
        user.getRole().toString()
    );
  }
}
