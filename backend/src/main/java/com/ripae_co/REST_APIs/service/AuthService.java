package com.digitalbanking.service;

import com.ripae_co.REST_APIs.dto.LoginRequest;
import com.ripae_co.REST_APIs.dto.LoginResponse;
import com.ripae_co.REST_APIs.dto.SignUpRequest;

public interface AuthService {
  LoginResponse login(LoginRequest request);
  void signup(SignUpRequest request);
  LoginResponse refreshToken(String token);
}
