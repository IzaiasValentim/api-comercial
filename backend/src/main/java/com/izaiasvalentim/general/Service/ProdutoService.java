package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Domain.Produto;
import com.izaiasvalentim.general.Repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // Método para recalcular estoque total (antigo updateResourceAfterChangedItemStock)
    @Transactional
    public void updateTotalStock(Produto produto) {
        try {
            produto.calculateTotalStock(); // Usa o método que criamos na Entidade Produto
            produtoRepository.save(produto);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro ao atualizar estoque do produto: " + e.getMessage());
        }
    }

    public Optional<Produto> findByCode(String code) {
        return produtoRepository.findByCode(code);

    }
}
