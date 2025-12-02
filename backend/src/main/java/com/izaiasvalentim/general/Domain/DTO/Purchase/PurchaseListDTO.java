package com.izaiasvalentim.general.Domain.DTO.Purchase;

import com.izaiasvalentim.general.Domain.Venda;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public record PurchaseListDTO(
        UUID id,
        String clientName,
        String clientCpf, // identificationNumber
        Double total,
        String status,
        LocalDateTime date,
        String sellerName
) {
    public PurchaseListDTO(Venda venda) {
        this(
                venda.getId(),
                venda.getClient().getName(),
                venda.getClient().getIdentificationNumber(),
                venda.getTotal(),
                venda.getStatus(), // Retorna a String do Enum
                venda.getRealizationDate() != null ?
                        LocalDateTime.ofInstant(venda.getRealizationDate().toInstant(), ZoneId.systemDefault()) : null,
                venda.getSeller().getUser().getUsername()
        );
    }
}
