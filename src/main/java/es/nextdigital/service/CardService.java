package es.nextdigital.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.nextdigital.model.Account;
import es.nextdigital.model.Card;
import es.nextdigital.model.CardType;
import es.nextdigital.model.Transaction;
import es.nextdigital.model.TransactionType;
import es.nextdigital.repository.AccountRepository;
import es.nextdigital.repository.CardRepository;
import es.nextdigital.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

   private final CardRepository cardRepository;

   private final AccountRepository accountRepository;

   private final TransactionRepository transactionRepository;

   private final PasswordEncoder passwordEncoder;

   public void activateCard(Long cardId) {
      Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
      card.setActive(true);
      String generatedPin = generateRandomPin(4);
      changePin(cardId, generatedPin);
      cardRepository.save(card);
   }

   public void changePin(Long cardId, String newPin) {
      Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
      card.setPinHash(passwordEncoder.encode(newPin));
      cardRepository.save(card);
   }

   private String generateRandomPin(int length) {
      Random random = new Random();
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < length; i++) {
         sb.append(random.nextInt(10));
      }
      return sb.toString();
   }

   public void withdraw(Long cardId, Double amount, boolean sameBank) {
      Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
      if (!card.isActive()) {
         throw new RuntimeException("Card not active");
      }

      if (!sameBank) {
         double commission = amount * 0.01;
         amount += commission;
      }

      Account acc = card.getAccount();

      if (card.getType() == CardType.DEBIT && acc.getBalance() < amount) {
         throw new RuntimeException("Insufficient balance");
      }
      if (card.getType() == CardType.CREDIT && amount > card.getLimitAmount()) {
         throw new RuntimeException("Credit limit exceeded");
      }

      acc.setBalance(acc.getBalance() - amount);
      accountRepository.save(acc);

      Transaction t = new Transaction(null, TransactionType.WITHDRAWAL.toString(), LocalDateTime.now(), amount, "ATM withdrawal", acc, null);
      transactionRepository.save(t);

      if (!sameBank) {
         double commission = amount * 0.01;
         Transaction commissionTransaction = new Transaction(null, TransactionType.COMMISSION.toString(), LocalDateTime.now(), commission, "Commission for other bank ATM", acc, null);
         transactionRepository.save(commissionTransaction);
      }
   }

   public void deposit(Long cardId, Double amount, boolean sameBank) {
      Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
      if (!card.isActive()) {
         throw new RuntimeException("Card not active");
      }
      if (!sameBank) {
         throw new RuntimeException("Deposit allowed only in same bank ATM");
      }

      Account acc = card.getAccount();
      acc.setBalance(acc.getBalance() + amount);
      accountRepository.save(acc);

      Transaction t = new Transaction(null, TransactionType.DEPOSIT.toString(), LocalDateTime.now(), amount, "ATM deposit", acc, null);
      transactionRepository.save(t);
   }
}

