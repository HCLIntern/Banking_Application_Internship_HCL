package com.digitalbanking.service.impl;

import com.digitalbanking.entity.User;
import com.digitalbanking.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private LoanRepository loanRepository;
  @Mock
  private PaymentRepository paymentRepository;
  @Mock
  private AssurancePolicyRepository assurancePolicyRepository;

  @InjectMocks
  private AdminServiceImpl adminService;

  @BeforeEach
  void setUp() { MockitoAnnotations.openMocks(this); }

  @Test
  void getDashboardStats_returnsCounts() {
    when(userRepository.count()).thenReturn(5L);
    when(accountRepository.count()).thenReturn(3L);
    when(transactionRepository.count()).thenReturn(10L);
    when(loanRepository.count()).thenReturn(2L);
    when(paymentRepository.count()).thenReturn(4L);
    when(assurancePolicyRepository.count()).thenReturn(1L);

    var res = adminService.getDashboardStats();
    assertNotNull(res);
  }

  @Test
  void disableUser_notFound_throws() {
    when(userRepository.findById(7L)).thenReturn(Optional.empty());
    assertThrows(RuntimeException.class, () -> adminService.disableUser(7L));
  }

  @Test
  void getAllUsers_empty_returnsEmptyList() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());
    var list = adminService.getAllUsers();
    assertNotNull(list);
  }
}
