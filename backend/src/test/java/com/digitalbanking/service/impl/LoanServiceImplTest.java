package com.digitalbanking.service.impl;

import com.digitalbanking.dto.LoanDTO;
import com.digitalbanking.entity.Loan;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.LoanRepository;
import com.digitalbanking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanServiceImplTest {

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private LoanServiceImpl loanService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void applyForLoan_userNotFound_shouldThrow() {
    when(userRepository.findById(5L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> loanService.applyForLoan(5L, new LoanDTO()));
  }

  @Test
  void applyForLoan_calculatesEmi_andPersists() {
    User u = new User();
    u.setId(2L);
    when(userRepository.findById(2L)).thenReturn(Optional.of(u));
    when(loanRepository.save(any(Loan.class))).thenAnswer(i -> {
      Loan l = i.getArgument(0);
      l.setId(99L);
      return l;
    });

    LoanDTO dto = LoanDTO.builder()
        .loanAmount(new BigDecimal("12000"))
        .interestRate(new BigDecimal("12"))
        .loanTermMonths(12)
        .build();

    LoanDTO created = loanService.applyForLoan(2L, dto);
    assertNotNull(created);
    assertEquals(99L, created.getId());
    assertNotNull(created.getEmiAmount());
    assertTrue(created.getEmiAmount().compareTo(BigDecimal.ZERO) > 0);
  }

  @Test
  void approveLoan_notFound_shouldThrow() {
    when(loanRepository.findById(7L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> loanService.approveLoan(7L));
  }
}
