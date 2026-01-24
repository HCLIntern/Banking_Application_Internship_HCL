package com.ripae_co.REST_APIs.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceDTO {
    private Long balanceId;
    private BigDecimal balanceAmount;
    private LocalDate balanceDate;
    private Long customerId;
    private Long accountId;
    private String remarks;
    private String crDrStatus; // "CR" or "DR"
    private BigDecimal crDrAmount;
}