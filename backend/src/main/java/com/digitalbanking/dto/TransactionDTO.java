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
public class TransactionDTO {
  private Long id;
  private Long fromAccountId;
  private Long toAccountId;
  private BigDecimal amount;
  private String description;
  private String type;
  private String status;
  private LocalDateTime timestamp;
  private String referenceNumber;
}
