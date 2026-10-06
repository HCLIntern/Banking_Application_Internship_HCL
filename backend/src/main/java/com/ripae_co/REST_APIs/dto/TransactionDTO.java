package com.ripae_co.REST_APIs.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long transactionId;
    private String transactionType;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private String remarks;
    private Long sourceCustomerId;
    private Long sourceAccountId;
    private Long targetCustomerId;
    private Long targetAccountId;
}