package es.nextdigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.nextdigital.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

