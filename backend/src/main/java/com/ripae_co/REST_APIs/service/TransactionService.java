package com.digitalbanking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ripae_co.REST_APIs.dto.TransactionDTO;

import java.util.List;

@Service
public interface TransactionService {
  Page<TransactionDTO> getUserTransactions(Long userId, Pageable pageable);
  TransactionDTO getTransactionById(Long transactionId);
  TransactionDTO transfer(Long fromAccountId, Long toAccountId, java.math.BigDecimal amount, String description);
  List<TransactionDTO> getAccountTransactions(Long accountId);
}
