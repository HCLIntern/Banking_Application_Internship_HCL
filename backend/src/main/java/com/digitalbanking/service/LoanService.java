package com.digitalbanking.service;

import com.digitalbanking.dto.LoanDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoanService {
  List<LoanDTO> getUserLoans(Long userId);
  LoanDTO getLoanById(Long loanId);
  LoanDTO applyForLoan(Long userId, LoanDTO dto);
  void approveLoan(Long loanId);
  void rejectLoan(Long loanId);
}
