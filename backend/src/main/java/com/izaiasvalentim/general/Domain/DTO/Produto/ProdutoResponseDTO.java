package com.izaiasvalentim.general.Domain.DTO.Produto;

import com.izaiasvalentim.general.Domain.Produto;

public record ProdutoResponseDTO(
        Long id,
        String code,
        String name,
        String category,
        Double price,      // Preço de referência (geralmente do lote mais recente ou média)
        Double totalStock, // Estoque total somado
        Boolean active
) {
    public ProdutoResponseDTO(Produto produto) {
        this(
                produto.getId(),
                produto.getCode(),
                produto.getName(),
                produto.getCategory(),
                // Pega o preço do primeiro lote ativo (se existir) como referência
                produto.getLotes().isEmpty() ? 0.0 : produto.getLotes().getFirst().getPrice(),
                produto.getTotalStock(),
                produto.getActive()
        );
    }
}