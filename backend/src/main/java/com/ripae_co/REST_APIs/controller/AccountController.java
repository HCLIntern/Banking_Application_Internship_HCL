package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Account;
import com.ripae_co.REST_APIs.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")

public class AccountController
{
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }
    @GetMapping
    public List<Account> getAllAccount()
    {
        return accountRepository.findAll();
    }
    @PostMapping
    public Account createAccount(@RequestBody Account account)
    {
        return accountRepository.save(account);
    }
}
