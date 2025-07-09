package es.nextdigital.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.nextdigital.demo.DemoApplication;
import es.nextdigital.model.Account;
import es.nextdigital.model.Card;
import es.nextdigital.model.CardType;
import es.nextdigital.repository.AccountRepository;
import es.nextdigital.repository.CardRepository;
import es.nextdigital.repository.TransactionRepository;

@SpringBootTest(classes = DemoApplication.class)
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

   @Mock
   private CardRepository cardRepository;

   @Mock
   private AccountRepository accountRepository;

   @Mock
   private TransactionRepository transactionRepository;

   @Mock
   private PasswordEncoder passwordEncoder;

   @InjectMocks
   private CardService cardService;

   @Test
   void testActivateCard() {
      Card card = new Card(1L, "12345678", "1234", true, CardType.DEBIT, 0.0, null);
      when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

      cardService.activateCard(1L);

      verify(cardRepository).save(card);
      assertTrue(card.isActive());
   }

   @Test
   void testChangePin() {
      Card card = new Card(1L, "12345678", "6666", true, CardType.DEBIT, 0.0, null);
      when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
      when(passwordEncoder.encode("1234")).thenReturn("1234");

      cardService.changePin(1L, "1234");

      verify(cardRepository).save(card);
      assertEquals("1234", card.getPinHash());
   }

   @Test
   void testWithdrawDebitSuccess() {
      Account acc = new Account(1L, "IBAN", 1000.0, null, null);
      Card card = new Card(1L, "12345678", "1234", true, CardType.DEBIT, 0.0, acc);
      when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
      when(accountRepository.save(any())).thenReturn(acc);

      cardService.withdraw(1L, 200.0, true);

      assertEquals(800.0, acc.getBalance());
      verify(transactionRepository).save(any());
   }

   @Test
   void testWithdrawDebitInsufficientBalance() {
      Account acc = new Account(1L, "IBAN", 100.0, null, null);
      Card card = new Card(1L, "12345678", "1234", true, CardType.DEBIT, 0.0, acc);
      when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

      Exception ex = assertThrows(RuntimeException.class, () -> cardService.withdraw(1L, 200.0, true));
      assertEquals("Insufficient balance", ex.getMessage());
   }
}

