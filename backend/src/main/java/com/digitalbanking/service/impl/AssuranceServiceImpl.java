package com.digitalbanking.service.impl;

import com.digitalbanking.dto.AssuranceDTO;
import com.digitalbanking.entity.AssurancePolicy;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.AssurancePolicyRepository;
import com.digitalbanking.repository.UserRepository;
import com.digitalbanking.service.AssuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssuranceServiceImpl implements AssuranceService {
  private final AssurancePolicyRepository policyRepository;
  private final UserRepository userRepository;

  @Override
  public List<AssuranceDTO> getUserPolicies(Long userId) {
    return policyRepository.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public AssuranceDTO getPolicyById(Long policyId) {
    AssurancePolicy p = policyRepository.findById(policyId)
        .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));
    return toDto(p);
  }

  @Override
  @Transactional
  public AssuranceDTO applyForAssurance(Long userId, AssuranceDTO dto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    String policyNumber = generatePolicyNumber();
    LocalDate start = dto.getStartDate() != null ? dto.getStartDate() : LocalDate.now();
    LocalDate expiry = dto.getExpiryDate() != null ? dto.getExpiryDate() : start.plusYears(1);

    AssurancePolicy policy = AssurancePolicy.builder()
        .user(user)
        .policyNumber(policyNumber)
        .productName(dto.getProductName())
        .premiumAmount(dto.getPremiumAmount())
        .coverageAmount(dto.getCoverageAmount())
        .startDate(start)
        .expiryDate(expiry)
        .status(AssurancePolicy.PolicyStatus.ACTIVE)
        .build();

    AssurancePolicy saved = policyRepository.save(policy);
    return toDto(saved);
  }

  @Override
  @Transactional
  public void renewPolicy(Long policyId) {
    AssurancePolicy p = policyRepository.findById(policyId)
        .orElseThrow(() -> new ResourceNotFoundException("Policy not found with id: " + policyId));

    LocalDate newStart = p.getExpiryDate().plusDays(1);
    LocalDate newExpiry = newStart.plusYears(1);
    p.setStartDate(newStart);
    p.setExpiryDate(newExpiry);
    p.setStatus(AssurancePolicy.PolicyStatus.RENEWED);
    policyRepository.save(p);
  }

  private AssuranceDTO toDto(AssurancePolicy p) {
    return AssuranceDTO.builder()
        .id(p.getId())
        .userId(p.getUser() != null ? p.getUser().getId() : null)
        .policyNumber(p.getPolicyNumber())
        .productName(p.getProductName())
        .premiumAmount(p.getPremiumAmount())
        .coverageAmount(p.getCoverageAmount())
        .startDate(p.getStartDate())
        .expiryDate(p.getExpiryDate())
        .status(p.getStatus() != null ? p.getStatus().name() : null)
        .build();
  }

  private String generatePolicyNumber() {
    return "P-" + UUID.randomUUID().toString().replaceAll("[^0-9A-Za-z]", "").substring(0, 12).toUpperCase();
  }
}
