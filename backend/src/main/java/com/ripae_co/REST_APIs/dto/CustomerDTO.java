package com.ripae_co.REST_APIs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private String customerType;
    private String notes;
}