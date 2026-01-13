package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Customer;
import com.ripae_co.REST_APIs.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")

public class CustomerController
{
    private final CustomerRepository customerRepository;

    public CustomerController (CustomerRepository customerRepository)
    {
        this.customerRepository=customerRepository;
    }
    @GetMapping
    public List<Customer> getAllCustomer()
    {
        return customerRepository.findAll();
    }
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer)
    {
        return customerRepository.save(customer);
    }

}
