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
public class PaymentDTO {
  private Long id;
  private Long accountId;
  private BigDecimal amount;
  private String description;
  private LocalDateTime paymentDate;
  private String status;
  private String referenceId;
  private LocalDateTime createdAt;
}
