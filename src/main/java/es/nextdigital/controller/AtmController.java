package es.nextdigital.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.nextdigital.dto.AmountDto;
import es.nextdigital.dto.PinDto;
import es.nextdigital.dto.TransferDto;
import es.nextdigital.model.Transaction;
import es.nextdigital.service.AccountService;
import es.nextdigital.service.CardService;
import es.nextdigital.service.TransferService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AtmController {

   private final CardService cardService;

   private final AccountService accountService;

   private final TransferService transferService;

   @GetMapping("/accounts/{id}/transactions")
   public List<Transaction> getTransactions(@PathVariable Long id) {
      return accountService.getTransactions(id);
   }

   @PostMapping("/cards/{id}/withdraw")
   public void withdraw(@PathVariable Long id, @RequestBody AmountDto amountDto) {
      cardService.withdraw(id, amountDto.getAmount(), amountDto.isSameBank());
   }

   @PostMapping("/cards/{id}/deposit")
   public void deposit(@PathVariable Long id, @RequestBody AmountDto amountDto) {
      cardService.deposit(id, amountDto.getAmount(), amountDto.isSameBank());
   }

   @PostMapping("/transfer")
   public void transfer(@RequestBody TransferDto transferDto) {
      transferService.transfer(transferDto);
   }

   @PostMapping("/cards/{id}/activate")
   public void activateCard(@PathVariable Long id) {
      cardService.activateCard(id);
   }

   @PostMapping("/cards/{id}/change-pin")
   public void changePin(@PathVariable Long id, @RequestBody PinDto pinDto) {
      cardService.changePin(id, pinDto.getNewPin());
   }
}

