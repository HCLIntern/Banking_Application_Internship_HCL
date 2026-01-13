package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Account__Balance;
import com.ripae_co.REST_APIs.repository.Account__BalanceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account__balance")
public class AccountBalanceController
{
   private final Account__BalanceRepository account__balanceRepository;

   public AccountBalanceController (Account__BalanceRepository account__balanceRepository)
   {
       this.account__balanceRepository=account__balanceRepository;
   }
   @GetMapping
    public List<Account__Balance> getAllAccountBalance()
   {
       return account__balanceRepository.findAll();
   }
   @PostMapping
    public Account__Balance createAccount_Balance(@RequestBody Account__Balance account__balance)
   {
       return account__balanceRepository.save(account__balance);
   }

}
