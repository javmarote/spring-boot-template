package es.nextdigital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String number;

   private String pinHash;

   private boolean active;

   @Enumerated(EnumType.STRING)
   private CardType type; // DEBIT, CREDIT

   private Double limitAmount;

   @ManyToOne
   @JoinColumn(name = "account_id")
   private Account account;

}

