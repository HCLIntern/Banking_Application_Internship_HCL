package com.digitalbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
  private Long id;
  private String accountNumber;
  private String accountType;
  private BigDecimal balance;
  private boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
