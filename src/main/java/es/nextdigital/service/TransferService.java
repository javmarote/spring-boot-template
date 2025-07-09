package es.nextdigital.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import es.nextdigital.dto.TransferDto;
import es.nextdigital.model.Account;
import es.nextdigital.model.Transaction;
import es.nextdigital.model.TransactionType;
import es.nextdigital.repository.AccountRepository;
import es.nextdigital.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {

   private final AccountRepository accountRepository;

   private final TransactionRepository transactionRepository;

   public void transfer(TransferDto transferDto) {
      Account originAccount = accountRepository
            .findById(transferDto.getFromAccountId())
            .orElseThrow(() -> new RuntimeException("Origin account not found"));

      Double amount = transferDto.getAmount().getAmount();

      if (!transferDto.getAmount().isSameBank()) {
         double commission = amount * 0.01;
         amount += commission;
      }

      if (originAccount.getBalance() < amount) {
         throw new RuntimeException("Insufficient balance");
      }

      // Simple IBAN validation
      if (!transferDto.getToIban().matches("[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}")) {
         throw new RuntimeException("Invalid IBAN");
      }

      originAccount.setBalance(originAccount.getBalance() - amount);
      accountRepository.save(originAccount);

      Transaction t = new Transaction(null, TransactionType.TRANSFER.toString(), LocalDateTime.now(), amount,
            "Transfer to " + transferDto.getToIban(), originAccount, transferDto.getToIban());
      transactionRepository.save(t);

      if (!transferDto.getAmount().isSameBank()) {
         double commission = amount * 0.01;
         Transaction commissionTransaction = new Transaction(null, TransactionType.COMMISSION.toString(), LocalDateTime.now(), commission,
               "Commission for other bank transfer", originAccount, null);
         transactionRepository.save(commissionTransaction);
      }
   }
}

