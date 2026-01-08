package com.digitalbanking.repository;

import com.digitalbanking.entity.AssurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssurancePolicyRepository extends JpaRepository<AssurancePolicy, Long> {
  List<AssurancePolicy> findByUserId(Long userId);
  Optional<AssurancePolicy> findByPolicyNumber(String policyNumber);
}
