package com.ripae_co.REST_APIs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDTO {
    private Long userId;
    private String userName;
    private String userType;
    private String remarks;
    private String passwordHash;
    private Long customerId;
}