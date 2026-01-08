package com.digitalbanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssuranceDTO {
  private Long id;
  private Long userId;
  private String policyNumber;
  private String productName;
  private BigDecimal premiumAmount;
  private BigDecimal coverageAmount;
  private LocalDate startDate;
  private LocalDate expiryDate;
  private String status;
}
