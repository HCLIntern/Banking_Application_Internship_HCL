package com.digitalbanking.service.impl;

import com.digitalbanking.dto.PaymentDTO;
import com.digitalbanking.entity.Account;
import com.digitalbanking.entity.Payment;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.AccountRepository;
import com.digitalbanking.repository.PaymentRepository;
import com.digitalbanking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final AccountRepository accountRepository;

  @Override
  @Transactional
  public PaymentDTO createPayment(Long accountId, BigDecimal amount, String description) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

    if (account.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient funds");
    }

    // Deduct balance
    account.setBalance(account.getBalance().subtract(amount));
    accountRepository.save(account);

    Payment payment = Payment.builder()
        .account(account)
        .amount(amount)
        .description(description)
        .paymentDate(LocalDateTime.now())
        .status(Payment.PaymentStatus.SUCCESS)
        .referenceId(UUID.randomUUID().toString())
        .createdAt(LocalDateTime.now())
        .build();

    Payment saved = paymentRepository.save(payment);
    return toDto(saved);
  }

  @Override
  public PaymentDTO getPaymentById(Long paymentId) {
    Payment p = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
    return toDto(p);
  }

  @Override
  public Page<PaymentDTO> getPaymentHistory(Long accountId, Pageable pageable) {
    Page<Payment> page = paymentRepository.findByAccountId(accountId, pageable);
    return page.map(this::toDto);
  }

  @Override
  public void updatePaymentStatus(Long paymentId, String status) {
    Payment p = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

    try {
      p.setStatus(Payment.PaymentStatus.valueOf(status.toUpperCase()));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid status value: " + status);
    }

    paymentRepository.save(p);
  }

  private PaymentDTO toDto(Payment p) {
    return PaymentDTO.builder()
        .id(p.getId())
        .accountId(p.getAccount() != null ? p.getAccount().getId() : null)
        .amount(p.getAmount())
        .description(p.getDescription())
        .paymentDate(p.getPaymentDate())
        .status(p.getStatus() != null ? p.getStatus().name() : null)
        .referenceId(p.getReferenceId())
        .createdAt(p.getCreatedAt())
        .build();
  }
}
