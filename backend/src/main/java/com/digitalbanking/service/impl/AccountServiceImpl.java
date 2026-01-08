package com.digitalbanking.service.impl;

import com.digitalbanking.dto.AccountDTO;
import com.digitalbanking.entity.Account;
import com.digitalbanking.entity.Transaction;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.AccountRepository;
import com.digitalbanking.repository.TransactionRepository;
import com.digitalbanking.repository.UserRepository;
import com.digitalbanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public List<AccountDTO> getUserAccounts(Long userId) {
    List<Account> accounts = accountRepository.findByUserId(userId);
    return accounts.stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public AccountDTO getAccountById(Long accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
    return toDto(account);
  }

  @Override
  public AccountDTO createAccount(Long userId, String accountType) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    Account.AccountType type;
    try {
      type = Account.AccountType.valueOf(accountType.toUpperCase());
    } catch (Exception e) {
      type = Account.AccountType.SAVINGS;
    }

    String acctNum = generateAccountNumber();

    Account account = Account.builder()
        .accountNumber(acctNum)
        .accountType(type)
        .balance(BigDecimal.ZERO)
        .user(user)
        .isActive(true)
        .createdAt(LocalDateTime.now())
        .build();

    Account saved = accountRepository.save(account);
    return toDto(saved);
  }

  @Override
  public void updateAccount(Long accountId, AccountDTO dto) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

    if (dto.getAccountType() != null) {
      try {
        account.setAccountType(Account.AccountType.valueOf(dto.getAccountType().toUpperCase()));
      } catch (Exception ignored) {}
    }

    if (dto.getBalance() != null) {
      account.setBalance(dto.getBalance());
    }

    account.setActive(dto.isActive());
    account.setUpdatedAt(LocalDateTime.now());

    accountRepository.save(account);
  }

  @Override
  public AccountDTO getAccountByNumber(String accountNumber) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
    return toDto(account);
  }

  @Override
  @Transactional
  public void transfer(Long fromAccountId, String toAccountNumber, BigDecimal amount, String description) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transfer amount must be greater than zero");
    }

    Account from = accountRepository.findById(fromAccountId)
        .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + fromAccountId));

    Account to = accountRepository.findByAccountNumber(toAccountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with number: " + toAccountNumber));

    if (from.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient funds");
    }

    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));

    accountRepository.save(from);
    accountRepository.save(to);

    Transaction tx = Transaction.builder()
        .fromAccount(from)
        .toAccount(to)
        .amount(amount)
        .description(description)
        .type(Transaction.TransactionType.TRANSFER)
        .status(Transaction.TransactionStatus.SUCCESS)
        .timestamp(LocalDateTime.now())
        .referenceNumber(UUID.randomUUID().toString())
        .build();

    transactionRepository.save(tx);
  }

  private AccountDTO toDto(Account a) {
    return AccountDTO.builder()
        .id(a.getId())
        .accountNumber(a.getAccountNumber())
        .accountType(a.getAccountType() != null ? a.getAccountType().name() : null)
        .balance(a.getBalance())
        .isActive(a.isActive())
        .createdAt(a.getCreatedAt())
        .updatedAt(a.getUpdatedAt())
        .build();
  }

  private String generateAccountNumber() {
    // Simple 12-digit numeric account number
    String raw = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
    if (raw.length() < 12) raw = String.format("%012d", Math.abs(raw.hashCode()));
    return raw.substring(0, 12);
  }
}
