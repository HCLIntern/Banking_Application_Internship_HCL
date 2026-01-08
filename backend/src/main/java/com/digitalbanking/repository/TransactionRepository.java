package com.digitalbanking.repository;

import com.digitalbanking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Page<Transaction> findByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId, Pageable pageable);
  List<Transaction> findByFromAccountIdOrderByTimestampDesc(Long accountId);
  List<Transaction> findByToAccountIdOrderByTimestampDesc(Long accountId);
  List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

  Page<Transaction> findByFromAccountIdInOrToAccountIdIn(Collection<Long> fromAccountIds, Collection<Long> toAccountIds, Pageable pageable);

  List<Transaction> findByFromAccountIdInOrderByTimestampDesc(Collection<Long> accountIds);

  List<Transaction> findByToAccountIdInOrderByTimestampDesc(Collection<Long> accountIds);
}
