package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Common.utils.ItemUtils;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemAddStockDTO;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import com.izaiasvalentim.general.Domain.DTO.Produto.ProdutoDetailDTO;
import com.izaiasvalentim.general.Domain.DTO.Produto.ProdutoResponseDTO;
import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Domain.Produto;
import com.izaiasvalentim.general.Repository.ItemRepository;
import com.izaiasvalentim.general.Repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, ProdutoRepository produtoRepository) {
        this.itemRepository = itemRepository;
        this.produtoRepository = produtoRepository;
    }

    // --- CRIAÇÃO ---

    @Transactional
    public ProdutoResponseDTO registerNewProduct(ItemDTO dto) {
        try {
            if (produtoRepository.findByCode(dto.code()).isPresent()) {
                throw new ResourceAlreadyExistsException("Produto com código " + dto.code() + " já existe.");
            }

            // 1. Cria o Produto (Pai)
            Produto produto = new Produto(dto);

            // Salva o pai -> Agora ele é persistente e tem ID
            Item firstBatch = new Item(dto);

            // Vincula ao pai já salvo
            firstBatch.setProduto(produto);

            // 3. Salva o item para gerar o ID (necessário para o Batch)
            firstBatch = itemRepository.save(firstBatch);

            // 4. Gera o código do lote com o ID real e atualiza
            ItemUtils.generateItemBatch(produto, firstBatch);
            itemRepository.save(firstBatch);

            // 5. Atualiza o estoque do pai e retorna
            // (Adicionamos na lista em memória apenas para o cálculo/retorno, pois já está salvo no banco)
            produto.getLotes().add(firstBatch);
            produto.calculateTotalStock();

            return new ProdutoResponseDTO(produtoRepository.save(produto));

        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro ao registrar produto: " + e.getMessage());
        }
    }

    @Transactional
    public ProdutoResponseDTO addStock(ItemAddStockDTO dto) {
        try {
            // Busca por código ou nome (preferência por código se tiver, mas mantive nome p/ compatibilidade)
            Produto produto = produtoRepository.findByCode(dto.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + dto.getCode()));

            // Cria novo lote
            Item newBatch = new Item(dto, produto);

            // Salva para gerar ID
            newBatch = itemRepository.save(newBatch);

            // Gera Batch Code e atualiza
            ItemUtils.generateItemBatch(produto, newBatch);
            itemRepository.save(newBatch);

            // Atualiza total do produto
            produto.getLotes().add(newBatch);
            produto.calculateTotalStock();

            return new ProdutoResponseDTO(produtoRepository.save(produto));

        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro ao adicionar estoque: " + e.getMessage());
        }
    }

    // --- LEITURA ---

    public List<ProdutoResponseDTO> getAllProductsPaged(int page, int size) {
        Page<Produto> produtos = produtoRepository.findAll(PageRequest.of(page, size));
        return produtos.stream().map(ProdutoResponseDTO::new).toList();
    }

    public ProdutoDetailDTO getProductDetailsByCode(String code) {
        Produto produto = produtoRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com código: " + code));
        return new ProdutoDetailDTO(produto);
    }

    public List<ProdutoResponseDTO> searchProductsByName(String name) {
        // Assumindo que você criou findByNameContaining no ProdutoRepository
        return produtoRepository.findByNameContaining(name)
                .stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }

    // --- EXCLUSÃO ---

    @Transactional
    public void deleteBatch(String batchCode) {
        Item batch = itemRepository.findByBatch(batchCode)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado: " + batchCode));

        batch.setDeleted(true);
        batch.setQuantity(0.0); // Zera quantidade para o cálculo bater
        itemRepository.save(batch);

        // Atualiza o pai
        Produto produto = batch.getProduto();
        produto.calculateTotalStock();
        produtoRepository.save(produto);
    }
}