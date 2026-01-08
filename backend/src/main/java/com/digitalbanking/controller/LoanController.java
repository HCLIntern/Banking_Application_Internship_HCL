package com.digitalbanking.controller;

import com.digitalbanking.dto.LoanDTO;
import com.digitalbanking.service.LoanService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.LOAN_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoanController {
  private final LoanService loanService;

  @GetMapping
  public ResponseEntity<List<LoanDTO>> getUserLoans(@RequestParam Long userId) {
    return ResponseEntity.ok(loanService.getUserLoans(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<LoanDTO> getLoanById(@PathVariable Long id) {
    return ResponseEntity.ok(loanService.getLoanById(id));
  }

  @PostMapping("/apply")
  public ResponseEntity<LoanDTO> applyForLoan(@RequestParam Long userId, @RequestBody LoanDTO dto) {
    return ResponseEntity.ok(loanService.applyForLoan(userId, dto));
  }

  @PutMapping("/{id}/approve")
  public ResponseEntity<?> approveLoan(@PathVariable Long id) {
    loanService.approveLoan(id);
    return ResponseEntity.ok("Loan approved");
  }

  @PutMapping("/{id}/reject")
  public ResponseEntity<?> rejectLoan(@PathVariable Long id) {
    loanService.rejectLoan(id);
    return ResponseEntity.ok("Loan rejected");
  }
}
