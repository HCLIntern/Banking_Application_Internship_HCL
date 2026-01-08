package com.digitalbanking.controller;

import com.digitalbanking.dto.AccountDTO;
import com.digitalbanking.service.AccountService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping(Constants.ACCOUNT_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
  private final AccountService accountService;

  @GetMapping
  public ResponseEntity<List<AccountDTO>> getUserAccounts(@RequestParam Long userId) {
    return ResponseEntity.ok(accountService.getUserAccounts(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.getAccountById(id));
  }

  @PostMapping
  public ResponseEntity<AccountDTO> createAccount(@RequestParam Long userId, @RequestParam String accountType) {
    return ResponseEntity.ok(accountService.createAccount(userId, accountType));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
    accountService.updateAccount(id, dto);
    return ResponseEntity.ok("Account updated successfully");
  }

  @GetMapping("/{id}/balance")
  public ResponseEntity<Object> getBalance(@PathVariable Long id) {
    return ResponseEntity.ok(accountService.getAccountById(id).getBalance());
  }

  @PostMapping("/transfer")
  public ResponseEntity<?> transfer(@RequestParam Long fromAccountId,
                                    @RequestParam String toAccountNumber,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam(required = false) String description) {
    accountService.transfer(fromAccountId, toAccountNumber, amount, description);
    return ResponseEntity.ok("Transfer completed");
  }
}
