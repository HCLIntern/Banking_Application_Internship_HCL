package com.digitalbanking.repository;

import com.digitalbanking.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
  List<Loan> findByUserId(Long userId);
  List<Loan> findByUserIdAndStatus(Long userId, Loan.LoanStatus status);
}
