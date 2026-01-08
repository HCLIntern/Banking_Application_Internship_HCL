package com.digitalbanking.repository;

import com.digitalbanking.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Page<Payment> findByAccountId(Long accountId, Pageable pageable);
  List<Payment> findByAccountIdOrderByPaymentDateDesc(Long accountId);
}
