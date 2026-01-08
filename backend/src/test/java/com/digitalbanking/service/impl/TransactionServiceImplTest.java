package com.digitalbanking.service.impl;

import com.digitalbanking.entity.Account;
import com.digitalbanking.entity.Transaction;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.AccountRepository;
import com.digitalbanking.repository.TransactionRepository;
import com.digitalbanking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TransactionServiceImpl transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void transfer_success() {
    Account from = Account.builder().id(1L).balance(new BigDecimal("200.00")).accountNumber("A1").build();
    Account to = Account.builder().id(2L).balance(new BigDecimal("50.00")).accountNumber("A2").build();

    when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
    when(accountRepository.findById(2L)).thenReturn(Optional.of(to));
    when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
      Transaction t = i.getArgument(0);
      t.setId(100L);
      return t;
    });

    var dto = transactionService.transfer(1L, 2L, new BigDecimal("25.00"), "ok");

    assertNotNull(dto);
    assertEquals(new BigDecimal("175.00"), from.getBalance());
    assertEquals(new BigDecimal("75.00"), to.getBalance());
    assertEquals(100L, dto.getId());
  }

  @Test
  void transfer_fromNotFound_shouldThrow() {
    when(accountRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> transactionService.transfer(1L, 2L, new BigDecimal("10.00"), "x"));
  }

  @Test
  void getUserTransactions_emptyAccounts_shouldReturnEmptyPage() {
    when(accountRepository.findByUserId(5L)).thenReturn(Collections.emptyList());
    Page<?> page = transactionService.getUserTransactions(5L, PageRequest.of(0, 10));
    assertTrue(page.isEmpty());
  }

  @Test
  void getAccountTransactions_mergesLists() {
    Transaction t1 = Transaction.builder().id(1L).timestamp(LocalDateTime.now()).build();
    Transaction t2 = Transaction.builder().id(2L).timestamp(LocalDateTime.now().minusDays(1)).build();
    when(transactionRepository.findByFromAccountIdOrderByTimestampDesc(1L)).thenReturn(Arrays.asList(t1));
    when(transactionRepository.findByToAccountIdOrderByTimestampDesc(1L)).thenReturn(Arrays.asList(t2));

    var list = transactionService.getAccountTransactions(1L);
    assertEquals(2, list.size());
  }
}
