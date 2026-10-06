package com.ripae_co.REST_APIs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private Long accountId;
    private Long customerId;
    private String accountName;
    private String accountType;
    private String remarks;
}
