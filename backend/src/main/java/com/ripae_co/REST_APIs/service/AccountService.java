package com.digitalbanking.service;

import org.springframework.stereotype.Service;

import com.ripae_co.REST_APIs.dto.AccountDTO;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface AccountService {
  List<AccountDTO> getUserAccounts(Long userId);
  AccountDTO getAccountById(Long accountId);
  AccountDTO createAccount(Long userId, String accountType);
  void updateAccount(Long accountId, AccountDTO dto);
  AccountDTO getAccountByNumber(String accountNumber);

  // Transfer amount from one account to another (atomic)
  void transfer(Long fromAccountId, String toAccountNumber, BigDecimal amount, String description);
}
