package com.digitalbanking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ripae_co.REST_APIs.dto.PaymentDTO;

import java.math.BigDecimal;

@Service
public interface PaymentService {
  PaymentDTO createPayment(Long accountId, BigDecimal amount, String description);
  PaymentDTO getPaymentById(Long paymentId);
  Page<PaymentDTO> getPaymentHistory(Long accountId, Pageable pageable);
  void updatePaymentStatus(Long paymentId, String status);
}
