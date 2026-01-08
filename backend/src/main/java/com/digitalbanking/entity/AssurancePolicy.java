package com.digitalbanking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "assurance_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssurancePolicy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, unique = true)
  private String policyNumber;

  private String productName;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal premiumAmount;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal coverageAmount;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate expiryDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PolicyStatus status;

  public enum PolicyStatus {
    ACTIVE, EXPIRED, CANCELLED, RENEWED
  }
}
