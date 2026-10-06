package com.ripae_co.REST_APIs.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.ripae_co.REST_APIs.dto.AccountDTO;
import com.ripae_co.REST_APIs.entity.Account;
import com.ripae_co.REST_APIs.entity.App_User;
import com.ripae_co.REST_APIs.repository.AccountRepository;
import com.ripae_co.REST_APIs.repository.App_UserRepository;
import com.ripae_co.REST_APIs.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountIntegrationTest {

  @Autowired
  private AccountService accountService;

  @Autowired
  private App_UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private User testUser;

  @BeforeEach
  void setUp() {
    accountRepository.deleteAll();
    userRepository.deleteAll();

    testUser = User.builder()
        .username("testuser")
        .email("test@example.com")
        .password(passwordEncoder.encode("password123"))
        .fullName("Test User")
        .role(User.UserRole.CUSTOMER)
        .isActive(true)
        .createdAt(LocalDateTime.now())
        .build();
    testUser = userRepository.save(testUser);
  }

  @Test
  void testCreateAccount() {
    AccountDTO dto = accountService.createAccount(testUser.getId(), "SAVINGS");
    assertNotNull(dto);
    assertEquals("SAVINGS", dto.getAccountType());
    assertEquals(BigDecimal.ZERO, dto.getBalance());
  }

  @Test
  void testGetUserAccounts() {
    accountService.createAccount(testUser.getId(), "SAVINGS");
    accountService.createAccount(testUser.getId(), "CHECKING");

    List<AccountDTO> accounts = accountService.getUserAccounts(testUser.getId());
    assertEquals(2, accounts.size());
  }

  @Test
  void testTransfer() {
    AccountDTO acc1 = accountService.createAccount(testUser.getId(), "SAVINGS");
    AccountDTO acc2 = accountService.createAccount(testUser.getId(), "CHECKING");

    // Deposit money to first account
    accountRepository.findById(acc1.getId()).ifPresent(a -> {
      a.setBalance(new BigDecimal("500.00"));
      accountRepository.save(a);
    });

    // Transfer
    accountService.transfer(acc1.getId(), acc2.getAccountNumber(), new BigDecimal("100.00"), "test");

    AccountDTO updated1 = accountService.getAccountById(acc1.getId());
    AccountDTO updated2 = accountService.getAccountById(acc2.getId());

    assertEquals(new BigDecimal("400.00"), updated1.getBalance());
    assertEquals(new BigDecimal("100.00"), updated2.getBalance());
  }
}
