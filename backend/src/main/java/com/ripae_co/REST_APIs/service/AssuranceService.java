package com.digitalbanking.service;

import org.springframework.stereotype.Service;

import com.ripae_co.REST_APIs.dto.AssuranceDTO;

import java.util.List;

@Service
public interface AssuranceService {
  List<AssuranceDTO> getUserPolicies(Long userId);
  AssuranceDTO getPolicyById(Long policyId);
  AssuranceDTO applyForAssurance(Long userId, AssuranceDTO dto);
  void renewPolicy(Long policyId);
}
