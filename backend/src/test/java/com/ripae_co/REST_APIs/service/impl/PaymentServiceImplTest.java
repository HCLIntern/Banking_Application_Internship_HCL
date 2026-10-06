package com.ripae_co.REST_APIs.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.ripae_co.REST_APIs.entity.Account;
import com.ripae_co.REST_APIs.entity.Payment;
import com.ripae_co.REST_APIs.exception.ResourceNotFoundException;
import com.ripae_co.REST_APIs.repository.AccountRepository;
import com.ripae_co.REST_APIs.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

  @Mock
  private PaymentRepository paymentRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private PaymentServiceImpl paymentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createPayment_success() {
    Account a = Account.builder().id(1L).balance(new BigDecimal("100.00")).build();
    when(accountRepository.findById(1L)).thenReturn(Optional.of(a));
    when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
    when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> {
      Payment p = i.getArgument(0);
      p.setId(10L);
      return p;
    });

    var dto = paymentService.createPayment(1L, new BigDecimal("20.00"), "bill");

    assertNotNull(dto);
    assertEquals(new BigDecimal("80.00"), a.getBalance());
    assertEquals(10L, dto.getId());
  }

  @Test
  void createPayment_insufficientFunds() {
    Account a = Account.builder().id(1L).balance(new BigDecimal("5.00")).build();
    when(accountRepository.findById(1L)).thenReturn(Optional.of(a));
    assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(1L, new BigDecimal("20.00"), "x"));
  }

  @Test
  void getPaymentById_notFound() {
    when(paymentRepository.findById(5L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(5L));
  }

  @Test
  void getPaymentHistory_pages() {
    Payment p = Payment.builder().id(1L).paymentDate(LocalDateTime.now()).build();
    when(paymentRepository.findByAccountId(1L, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(java.util.List.of(p)));
    Page<?> page = paymentService.getPaymentHistory(1L, PageRequest.of(0, 10));
    assertFalse(page.isEmpty());
  }
}
