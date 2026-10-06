package com.ripae_co.REST_APIs.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerKYCDTO {
    private Long kycId;
    private Long customerId;
    private String address1;
    private String address2;
    private String mailId;
    private LocalDate dob;
    private String notes;
}