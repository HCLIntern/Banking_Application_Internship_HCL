package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Transactions;
import com.ripae_co.REST_APIs.repository.TransactionsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")

public class TransactionsController
{
    private final TransactionsRepository transactionsRepository;

    public TransactionsController (TransactionsRepository transactionsRepository)
    {
        this.transactionsRepository=transactionsRepository;
    }
    @GetMapping
    public List<Transactions> getallTransactions()
    {
        return transactionsRepository.findAll();
    }
    @PostMapping
    public Transactions createTransactions(@RequestBody Transactions transactions)
    {
        return transactionsRepository.save(transactions);
    }
}

