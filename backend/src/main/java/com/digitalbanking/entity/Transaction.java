package com.digitalbanking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_account_id")
  private Account fromAccount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_account_id")
  private Account toAccount;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionStatus status;

  @Column(nullable = false, updatable = false)
  private LocalDateTime timestamp;

  private String referenceNumber;

  public enum TransactionType {
    TRANSFER, DEPOSIT, WITHDRAWAL, PAYMENT
  }

  public enum TransactionStatus {
    SUCCESS, PENDING, FAILED
  }
}
