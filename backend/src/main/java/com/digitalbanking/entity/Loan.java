package com.digitalbanking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal loanAmount;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal principalAmount;

  @Column(nullable = false)
  private BigDecimal interestRate;

  @Column(nullable = false)
  private Integer loanTermMonths;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal emiAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LoanStatus status;

  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate disbursedDate;

  public enum LoanStatus {
    PENDING, APPROVED, REJECTED, ACTIVE, CLOSED, DEFAULT
  }
}
