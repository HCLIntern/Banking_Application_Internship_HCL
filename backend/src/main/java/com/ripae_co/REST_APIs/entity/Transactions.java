package com.ripae_co.REST_APIs.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")

public class Transactions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transaction_ID",nullable = false)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "Source_Customer_ID", referencedColumnName = "Customer_ID")
    private Customer sourcecustomer;

    @ManyToOne
    @JoinColumn(name = "Target_Customer_ID", referencedColumnName = "Customer_ID")
    private Customer targetcustomer;

    @ManyToOne
    @JoinColumn(name = "Source_Account_ID", referencedColumnName = "Account_ID")
    private Account sourceaccount;

    @ManyToOne
    @JoinColumn(name = "Target_Account_ID", referencedColumnName = "Account_ID")
    private Account targetaccount;

    @Column(name = "Transaction_Amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "Transaction_Date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "Transaction_Type", nullable = false)
    private String transactionType;

    @Column(name = "Remarks")
    private String remarks;
}
