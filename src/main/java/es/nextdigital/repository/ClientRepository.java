package es.nextdigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.nextdigital.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {}

