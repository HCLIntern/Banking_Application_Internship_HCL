package com.digitalbanking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ripae_co.REST_APIs.dto.AdminUserDTO;
import com.ripae_co.REST_APIs.entity.AssurancePolicy;
import com.ripae_co.REST_APIs.entity.Loan;
import com.ripae_co.REST_APIs.entity.User;
import com.ripae_co.REST_APIs.repository.*;
import com.ripae_co.REST_APIs.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
  private final UserRepository userRepository;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;
  private final LoanRepository loanRepository;
  private final PaymentRepository paymentRepository;
  private final AssurancePolicyRepository assurancePolicyRepository;

  @Override
  public Object getDashboardStats() {
    Map<String, Object> stats = new HashMap<>();
    stats.put("users", userRepository.count());
    stats.put("accounts", accountRepository.count());
    stats.put("transactions", transactionRepository.count());
    stats.put("loans", loanRepository.count());
    stats.put("payments", paymentRepository.count());
    stats.put("policies", assurancePolicyRepository.count());
    return stats;
  }

  @Override
  public Object getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void disableUser(Long userId) {
    User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    u.setActive(false);
    userRepository.save(u);
  }

  @Override
  @Transactional
  public void enableUser(Long userId) {
    User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    u.setActive(true);
    userRepository.save(u);
  }

  @Override
  public Object generateReport(String reportType) {
    // Simple reports: "users", "transactions", "loans", "payments", "policies"
    switch ((reportType == null) ? "" : reportType.toLowerCase()) {
      case "users":
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
      case "loans":
        List<Loan> loans = loanRepository.findAll();
        return loans;
      case "payments":
        return paymentRepository.findAll();
      case "policies":
        List<AssurancePolicy> policies = assurancePolicyRepository.findAll();
        return policies;
      default:
        Map<String, Object> basic = new HashMap<>();
        basic.put("message", "Unknown report type: " + reportType);
        return basic;
    }
  }

  private AdminUserDTO toDto(User u) {
    return AdminUserDTO.builder()
        .id(u.getId())
        .username(u.getUsername())
        .email(u.getEmail())
        .fullName(u.getFullName())
        .role(u.getRole() != null ? u.getRole().name() : null)
        .isActive(u.isActive())
        .createdAt(u.getCreatedAt())
        .build();
  }
}
