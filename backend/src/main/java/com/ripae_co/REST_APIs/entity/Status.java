package com.ripae_co.REST_APIs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "status")

public class Status
{
    @Id
    @Column(name = "STATUS_ID", nullable = false)
    private Integer statusId;

    @ManyToOne
    @JoinColumn(name = "Customer_ID", nullable = false)
    private Customer customer;

    @Column(name = "REQUEST_STATUS", nullable = false)
    private String requestStatus;

}
