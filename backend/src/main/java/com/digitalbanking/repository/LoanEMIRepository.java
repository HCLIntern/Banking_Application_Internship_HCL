package com.digitalbanking.repository;

import com.digitalbanking.entity.LoanEMI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanEMIRepository extends JpaRepository<LoanEMI, Long> {
  List<LoanEMI> findByLoanId(Long loanId);
  List<LoanEMI> findByLoanIdAndStatus(Long loanId, LoanEMI.EMIStatus status);
}
