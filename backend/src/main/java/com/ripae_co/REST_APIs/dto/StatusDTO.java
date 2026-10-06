package com.ripae_co.REST_APIs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusDTO {
    private String statusId;
    private Long customerId;
    private String requestStatus;
}