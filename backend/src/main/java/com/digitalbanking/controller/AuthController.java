package com.digitalbanking.controller;

import com.digitalbanking.dto.ApiResponse;
import com.digitalbanking.dto.LoginRequest;
import com.digitalbanking.dto.LoginResponse;
import com.digitalbanking.dto.SignUpRequest;
import com.digitalbanking.service.AuthService;
import com.digitalbanking.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.AUTH_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
  private final AuthService authService;

  /**
   * Authenticates user and returns JWT token.
   *
   * @param request LoginRequest with username and password
   * @return ApiResponse with LoginResponse containing JWT token
   */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
    try {
      LoginResponse response = authService.login(request);
      return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  /**
   * Registers a new user account.
   *
   * @param request SignUpRequest with user registration details
   * @return ApiResponse with success message
   */
  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody SignUpRequest request) {
    try {
      authService.signup(request);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(ApiResponse.success("User registered successfully. Please login.", null));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponse.error(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ApiResponse.error("Registration failed. Please try again."));
    }
  }

  /**
   * Refreshes JWT token for authenticated user.
   *
   * @param authHeader Authorization header with Bearer token
   * @return ApiResponse with new LoginResponse containing refreshed JWT token
   */
  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestHeader("Authorization") String authHeader) {
    try {
      LoginResponse response = authService.refreshToken(authHeader);
      return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
