package com.digitalbanking.service.impl;

import com.digitalbanking.dto.AssuranceDTO;
import com.digitalbanking.entity.AssurancePolicy;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.AssurancePolicyRepository;
import com.digitalbanking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AssuranceServiceImplTest {

  @Mock
  private AssurancePolicyRepository policyRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private AssuranceServiceImpl assuranceService;

  @BeforeEach
  void setUp() { MockitoAnnotations.openMocks(this); }

  @Test
  void applyForAssurance_userNotFound_shouldThrow() {
    when(userRepository.findById(5L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> assuranceService.applyForAssurance(5L, new AssuranceDTO()));
  }

  @Test
  void applyForAssurance_success() {
    User u = new User(); u.setId(2L);
    when(userRepository.findById(2L)).thenReturn(Optional.of(u));
    when(policyRepository.save(any(AssurancePolicy.class))).thenAnswer(i -> {
      AssurancePolicy p = i.getArgument(0);
      p.setId(11L);
      return p;
    });

    AssuranceDTO dto = AssuranceDTO.builder()
        .productName("Health Plan")
        .premiumAmount(new BigDecimal("100.00"))
        .coverageAmount(new BigDecimal("10000.00"))
        .startDate(LocalDate.now())
        .build();

    var created = assuranceService.applyForAssurance(2L, dto);
    assertNotNull(created);
    assertEquals(11L, created.getId());
    assertEquals("Health Plan", created.getProductName());
  }

  @Test
  void renewPolicy_notFound_shouldThrow() {
    when(policyRepository.findById(7L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> assuranceService.renewPolicy(7L));
  }
}
