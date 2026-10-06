package com.ripae_co.REST_APIs.repository;

import com.ripae_co.REST_APIs.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

