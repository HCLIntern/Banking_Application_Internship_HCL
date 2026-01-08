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
public class LoanDTO {
  private Long id;
  private Long userId;
  private BigDecimal loanAmount;
  private BigDecimal principalAmount;
  private BigDecimal interestRate;
  private Integer loanTermMonths;
  private BigDecimal emiAmount;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate disbursedDate;
}
