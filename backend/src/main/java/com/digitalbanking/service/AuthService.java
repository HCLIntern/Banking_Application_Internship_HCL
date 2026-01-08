package com.digitalbanking.service;

import com.digitalbanking.dto.LoginRequest;
import com.digitalbanking.dto.LoginResponse;
import com.digitalbanking.dto.SignUpRequest;

public interface AuthService {
  LoginResponse login(LoginRequest request);
  void signup(SignUpRequest request);
  LoginResponse refreshToken(String token);
}
