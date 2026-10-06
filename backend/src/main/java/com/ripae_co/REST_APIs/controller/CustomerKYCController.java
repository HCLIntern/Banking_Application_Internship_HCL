package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Customer_KYC;
import com.ripae_co.REST_APIs.repository.Customer_KYCRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer_kyc")

public class CustomerKYCController
{
    private final Customer_KYCRepository customerKycRepository;

    public CustomerKYCController (Customer_KYCRepository customerKycRepository)
    {
        this.customerKycRepository=customerKycRepository;
    }
    @GetMapping
    public List<Customer_KYC> getAllCustomer_KYC()
    {
        return customerKycRepository.findAll();
    }
    @PostMapping
    public Customer_KYC createCustomer_KYC(Customer_KYC customerKyc)
    {
        return customerKycRepository.save(customerKyc);
    }
}
