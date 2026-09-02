package com.dersonlopes.picpaysimplificado.repositories;

import com.dersonlopes.picpaysimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
