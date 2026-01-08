package com.digitalbanking.controller;

import com.digitalbanking.dto.TransactionDTO;
import com.digitalbanking.service.TransactionService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(Constants.TRANSACTION_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<Page<TransactionDTO>> getTransactions(
      @RequestParam Long userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(transactionService.getUserTransactions(userId, PageRequest.of(page, size)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
    return ResponseEntity.ok(transactionService.getTransactionById(id));
  }

  @PostMapping("/transfer")
  public ResponseEntity<TransactionDTO> transfer(
      @RequestParam Long fromAccountId,
      @RequestParam Long toAccountId,
      @RequestParam BigDecimal amount,
      @RequestParam(required = false) String description) {
    return ResponseEntity.ok(transactionService.transfer(fromAccountId, toAccountId, amount, description));
  }

  @GetMapping("/account/{accountId}")
  public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable Long accountId) {
    return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
  }
}
