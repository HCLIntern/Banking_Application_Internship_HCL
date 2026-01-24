package com.ripae_co.REST_APIs.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ripae_co.REST_APIs.dto.TransactionDTO;
import com.ripae_co.REST_APIs.entity.Account;
import com.ripae_co.REST_APIs.entity.Transaction;
import com.ripae_co.REST_APIs.exception.ResourceNotFoundException;
import com.ripae_co.REST_APIs.repository.AccountRepository;
import com.ripae_co.REST_APIs.repository.TransactionRepository;
import com.ripae_co.REST_APIs.repository.UserRepository;
import com.ripae_co.REST_APIs.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  @Override
  public Page<TransactionDTO> getUserTransactions(Long userId, Pageable pageable) {
    // find user's accounts
    List<Account> accounts = accountRepository.findByUserId(userId);
    List<Long> ids = accounts.stream().map(Account::getId).collect(Collectors.toList());
    if (ids.isEmpty()) {
      return Page.empty(pageable);
    }
    Page<Transaction> page = transactionRepository.findByFromAccountIdInOrToAccountIdIn(ids, ids, pageable);
    return page.map(this::toDto);
  }

  @Override
  public TransactionDTO getTransactionById(Long transactionId) {
    Transaction tx = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
    return toDto(tx);
  }

  @Override
  @Transactional
  public TransactionDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }

    Account from = accountRepository.findById(fromAccountId)
        .orElseThrow(() -> new ResourceNotFoundException("Source account not found with id: " + fromAccountId));
    Account to = accountRepository.findById(toAccountId)
        .orElseThrow(() -> new ResourceNotFoundException("Destination account not found with id: " + toAccountId));

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

    Transaction saved = transactionRepository.save(tx);
    return toDto(saved);
  }

  @Override
  public List<TransactionDTO> getAccountTransactions(Long accountId) {
    List<Transaction> from = transactionRepository.findByFromAccountIdOrderByTimestampDesc(accountId);
    List<Transaction> to = transactionRepository.findByToAccountIdOrderByTimestampDesc(accountId);
    // merge by timestamp desc (simple approach: append and sort)
    List<Transaction> merged = from;
    merged.addAll(to);
    merged.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
    return merged.stream().map(this::toDto).collect(Collectors.toList());
  }

  private TransactionDTO toDto(Transaction t) {
    return TransactionDTO.builder()
        .id(t.getId())
        .fromAccountId(t.getFromAccount() != null ? t.getFromAccount().getId() : null)
        .toAccountId(t.getToAccount() != null ? t.getToAccount().getId() : null)
        .amount(t.getAmount())
        .description(t.getDescription())
        .type(t.getType() != null ? t.getType().name() : null)
        .status(t.getStatus() != null ? t.getStatus().name() : null)
        .timestamp(t.getTimestamp())
        .referenceNumber(t.getReferenceNumber())
        .build();
  }
}
