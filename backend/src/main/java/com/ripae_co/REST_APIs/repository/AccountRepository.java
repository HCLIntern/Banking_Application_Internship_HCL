package com.ripae_co.REST_APIs.repository;

import com.ripae_co.REST_APIs.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>
{
}
