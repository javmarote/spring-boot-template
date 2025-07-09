package es.nextdigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.nextdigital.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {}

