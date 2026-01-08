package com.digitalbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
  private String token;
  private String type;
  private Long id;
  private String username;
  private String email;
  private String fullName;
  private String role;

  public LoginResponse(String token, Long id, String username, String email, String fullName, String role) {
    this.token = token;
    this.type = "Bearer";
    this.id = id;
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.role = role;
  }
}
