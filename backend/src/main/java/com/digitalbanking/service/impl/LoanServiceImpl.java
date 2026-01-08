package com.digitalbanking.service.impl;

import com.digitalbanking.dto.LoanDTO;
import com.digitalbanking.entity.Loan;
import com.digitalbanking.entity.User;
import com.digitalbanking.exception.ResourceNotFoundException;
import com.digitalbanking.repository.LoanRepository;
import com.digitalbanking.repository.UserRepository;
import com.digitalbanking.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
  private final LoanRepository loanRepository;
  private final UserRepository userRepository;

  @Override
  public List<LoanDTO> getUserLoans(Long userId) {
    return loanRepository.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public LoanDTO getLoanById(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
    return toDto(loan);
  }

  @Override
  @Transactional
  public LoanDTO applyForLoan(Long userId, LoanDTO dto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    BigDecimal loanAmount = dto.getLoanAmount() != null ? dto.getLoanAmount() : BigDecimal.ZERO;
    BigDecimal interestRate = dto.getInterestRate() != null ? dto.getInterestRate() : BigDecimal.ZERO; // annual percent
    Integer termMonths = dto.getLoanTermMonths() != null ? dto.getLoanTermMonths() : 12;

    BigDecimal emi = calculateEmi(loanAmount, interestRate, termMonths);

    Loan loan = Loan.builder()
        .user(user)
        .loanAmount(loanAmount)
        .principalAmount(loanAmount)
        .interestRate(interestRate)
        .loanTermMonths(termMonths)
        .emiAmount(emi)
        .status(Loan.LoanStatus.PENDING)
        .build();

    Loan saved = loanRepository.save(loan);
    return toDto(saved);
  }

  @Override
  @Transactional
  public void approveLoan(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));

    loan.setStatus(Loan.LoanStatus.APPROVED);
    LocalDate now = LocalDate.now();
    loan.setDisbursedDate(now);
    loan.setStartDate(now);
    loan.setEndDate(now.plusMonths(loan.getLoanTermMonths()));
    loanRepository.save(loan);
  }

  @Override
  @Transactional
  public void rejectLoan(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
    loan.setStatus(Loan.LoanStatus.REJECTED);
    loanRepository.save(loan);
  }

  private LoanDTO toDto(Loan l) {
    return LoanDTO.builder()
        .id(l.getId())
        .userId(l.getUser() != null ? l.getUser().getId() : null)
        .loanAmount(l.getLoanAmount())
        .principalAmount(l.getPrincipalAmount())
        .interestRate(l.getInterestRate())
        .loanTermMonths(l.getLoanTermMonths())
        .emiAmount(l.getEmiAmount())
        .status(l.getStatus() != null ? l.getStatus().name() : null)
        .startDate(l.getStartDate())
        .endDate(l.getEndDate())
        .disbursedDate(l.getDisbursedDate())
        .build();
  }

  private BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualInterestPercent, int months) {
    if (principal == null || principal.compareTo(BigDecimal.ZERO) == 0 || months <= 0) {
      return BigDecimal.ZERO;
    }
    if (annualInterestPercent == null || annualInterestPercent.compareTo(BigDecimal.ZERO) == 0) {
      return principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
    }

    // r = monthly rate in decimal
    BigDecimal monthlyRate = annualInterestPercent.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
    // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
    double r = monthlyRate.doubleValue();
    int n = months;
    double p = principal.doubleValue();

    double factor = Math.pow(1 + r, n);
    double emiDouble = p * r * factor / (factor - 1);
    BigDecimal emi = BigDecimal.valueOf(emiDouble).setScale(2, RoundingMode.HALF_UP);
    return emi;
  }
}
