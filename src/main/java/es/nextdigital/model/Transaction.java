package es.nextdigital.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
public class Transaction {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String type; // WITHDRAWAL, DEPOSIT, TRANSFER, COMMISSION

   private LocalDateTime date;

   private Double amount;

   private String description;

   @ManyToOne
   @JoinColumn(name = "account_id")
   private Account account;

   private String destinationIban; // Solo para transferencias
}

