package com.digitalbanking.controller;

import com.digitalbanking.dto.PaymentDTO;
import com.digitalbanking.service.PaymentService;
import com.digitalbanking.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(Constants.PAYMENT_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping
  public ResponseEntity<PaymentDTO> createPayment(
      @RequestParam Long accountId,
      @RequestParam BigDecimal amount,
      @RequestParam(required = false) String description) {
    return ResponseEntity.ok(paymentService.createPayment(accountId, amount, description));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
    return ResponseEntity.ok(paymentService.getPaymentById(id));
  }

  @GetMapping
  public ResponseEntity<Page<PaymentDTO>> getPaymentHistory(
      @RequestParam Long accountId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(paymentService.getPaymentHistory(accountId, PageRequest.of(page, size)));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
    paymentService.updatePaymentStatus(id, status);
    return ResponseEntity.ok("Payment status updated");
  }
}
