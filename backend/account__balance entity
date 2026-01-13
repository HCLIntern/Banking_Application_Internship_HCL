package com.ripae_co.REST_APIs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account__balance")
public class Account__Balance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Balance_ID")
    private Integer balanceId;

    @ManyToOne
    @JoinColumn(name = "Customer_ID", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "Account_ID", nullable = false)
    private Account account;

    @Column(name = "Balance_Amount", nullable = false)
    private BigDecimal balanceAmount;

    @Column(name = "Balance_Date", nullable = false)
    private LocalDate balanceDate;

    @Column(name = "CR_DR_Status", nullable = false)
    private String crDrStatus;

    @Column(name = "CR_DR_AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal crDrAmount;

    @Column(name = "Remarks")
    private String Remarks;
}
