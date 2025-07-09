package es.nextdigital.dto;

import lombok.Data;

@Data
public class TransferDto {
   private Long fromAccountId;
   private String toIban;
   private AmountDto amount;
}

