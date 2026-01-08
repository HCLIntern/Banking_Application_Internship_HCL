package com.digitalbanking.service;

import com.digitalbanking.dto.AssuranceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssuranceService {
  List<AssuranceDTO> getUserPolicies(Long userId);
  AssuranceDTO getPolicyById(Long policyId);
  AssuranceDTO applyForAssurance(Long userId, AssuranceDTO dto);
  void renewPolicy(Long policyId);
}
