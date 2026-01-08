package com.digitalbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDTO {
  private Long id;
  private String username;
  private String email;
  private String fullName;
  private String role;
  private boolean isActive;
  private LocalDateTime createdAt;
}
