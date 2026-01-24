package com.ripae_co.REST_APIs.repository;

import com.ripae_co.REST_APIs.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}
