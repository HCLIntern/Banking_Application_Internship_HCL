package com.digitalbanking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  private String description;

  @Column(nullable = false)
  private LocalDateTime paymentDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  private String referenceId;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public enum PaymentStatus {
    SUCCESS, PENDING, FAILED
  }
}
