package com.izaiasvalentim.general.Domain.DTO.Produto;

import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Domain.Produto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record ProdutoDetailDTO(
        Long id,
        String code,
        String name,
        String category,
        Double totalStock,
        List<BatchDTO> batches
) {
    public ProdutoDetailDTO(Produto produto) {
        this(
                produto.getId(),
                produto.getCode(),
                produto.getName(),
                produto.getCategory(),
                produto.getTotalStock(),
                produto.getLotes().stream()
                        .filter(i -> !i.getDeleted()) // Filtra lotes deletados
                        .map(BatchDTO::new)
                        .collect(Collectors.toList())
        );
    }

    // DTO interno para representar apenas o Lote
    public record BatchDTO(
            String batch,
            Double price,
            Double quantity,
            Date validity,
            Boolean hasValidity
    ) {
        public BatchDTO(Item item) {
            this(
                    item.getBatch(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getValidity(),
                    item.getHasValidity()
            );
        }
    }
}