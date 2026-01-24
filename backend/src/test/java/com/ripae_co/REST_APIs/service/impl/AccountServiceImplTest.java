package com.ripae_co.REST_APIs.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ripae_co.REST_APIs.entity.Account;
import com.ripae_co.REST_APIs.entity.Transaction;
import com.ripae_co.REST_APIs.entity.User;
import com.ripae_co.REST_APIs.exception.ResourceNotFoundException;
import com.ripae_co.REST_APIs.repository.AccountRepository;
import com.ripae_co.REST_APIs.repository.TransactionRepository;
import com.ripae_co.REST_APIs.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private AccountServiceImpl accountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createAccount_whenUserNotFound_shouldThrow() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(1L, "SAVINGS"));
  }

  @Test
  void transfer_success() {
    Account from = Account.builder().id(1L).balance(new BigDecimal("100.00")).accountNumber("111").build();
    Account to = Account.builder().id(2L).balance(new BigDecimal("50.00")).accountNumber("222").build();

    when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
    when(accountRepository.findByAccountNumber("222")).thenReturn(Optional.of(to));
    when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

    accountService.transfer(1L, "222", new BigDecimal("25.00"), "test transfer");

    assertEquals(new BigDecimal("75.00"), from.getBalance());
    assertEquals(new BigDecimal("75.00"), to.getBalance());
    verify(transactionRepository, times(1)).save(any(Transaction.class));
  }

  @Test
  void transfer_insufficientFunds_shouldThrow() {
    Account from = Account.builder().id(1L).balance(new BigDecimal("10.00")).accountNumber("111").build();
    Account to = Account.builder().id(2L).balance(new BigDecimal("50.00")).accountNumber("222").build();

    when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
    when(accountRepository.findByAccountNumber("222")).thenReturn(Optional.of(to));

    assertThrows(IllegalArgumentException.class, () -> accountService.transfer(1L, "222", new BigDecimal("25.00"), "t"));
    verify(transactionRepository, times(0)).save(any(Transaction.class));
  }
}
