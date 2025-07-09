package es.nextdigital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.nextdigital.model.Account;
import es.nextdigital.model.Transaction;
import es.nextdigital.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

   private final AccountRepository accountRepository;

   public List<Transaction> getTransactions(Long accountId) {
      Account acc = accountRepository.findById(accountId)
                                     .orElseThrow(() -> new RuntimeException("Account not found"));
      return acc.getTransactions();
   }
}

