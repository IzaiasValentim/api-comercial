package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Item.ItemAddStockDTO;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import com.izaiasvalentim.general.Domain.DTO.Produto.ProdutoDetailDTO;
import com.izaiasvalentim.general.Domain.DTO.Produto.ProdutoResponseDTO;
import com.izaiasvalentim.general.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 1. Listar Produtos (Paginado) - Substitui getAllAgregated
    @GetMapping("getAllAgregated")
    public ResponseEntity<List<ProdutoResponseDTO>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "quantidade", defaultValue = "10") int quantidade) {

        return ResponseEntity.ok(itemService.getAllProductsPaged(page, quantidade));
    }

    // 2. Detalhes do Produto (Com lotes) - Substitui itemStockByCode
    @GetMapping(value = "itemStockByCode")
    public ResponseEntity<ProdutoDetailDTO> getProductDetails(@RequestParam String code) {
        return ResponseEntity.ok(itemService.getProductDetailsByCode(code));
    }

    // 3. Criar Novo Produto
    @PostMapping(value = "/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<ProdutoResponseDTO> createProduct(@RequestBody ItemDTO dto) {
        return new ResponseEntity<>(itemService.registerNewProduct(dto), HttpStatus.CREATED);
    }

    // 4. Adicionar Estoque
    @PostMapping(value = "addStockByCode/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<ProdutoResponseDTO> addStock(@RequestBody ItemAddStockDTO dto) {
        return new ResponseEntity<>(itemService.addStock(dto), HttpStatus.CREATED);
    }

    // 5. Excluir Lote
    @DeleteMapping(value = "deleteByBatch")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<Void> deleteBatch(@RequestParam String batch) {
        itemService.deleteBatch(batch);
        return ResponseEntity.ok().build();
    }

    // 6. Buscar por Nome (Opcional, para autocomplete)
    @GetMapping(value = "allByName")
    public ResponseEntity<List<ProdutoResponseDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(itemService.searchProductsByName(name));
    }
}
