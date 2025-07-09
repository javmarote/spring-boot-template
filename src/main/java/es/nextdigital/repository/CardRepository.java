package es.nextdigital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.nextdigital.model.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}

