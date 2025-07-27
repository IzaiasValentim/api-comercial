package com.izaiasvalentim.general.Controller;

import com.izaiasvalentim.general.Domain.DTO.Item.ItemAddStockDTO;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemAgregadoResponseDTO;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Service.ItemAgregadoService;
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
    private final ItemAgregadoService itemAgregadoService;

    @Autowired
    public ItemController(ItemService itemService, ItemAgregadoService itemAgregadoService) {
        this.itemService = itemService;
        this.itemAgregadoService = itemAgregadoService;
    }

    @GetMapping(value = "allByName")
    @PreAuthorize(
                    "hasAuthority('SCOPE_MANAGER') || " +
                    "hasAuthority('SCOPE_SELLER') || " +
                    "hasAuthority('SCOPE_INTERN') ")
    public ResponseEntity<?> getAllItems(@RequestParam String name) {
        var listItems = itemService.getAllItemsByName(name);

        return new ResponseEntity<>(listItems, HttpStatus.OK);
    }


    @GetMapping(value = "itemStockByCode")
    public ResponseEntity<?> getItemStockByCode(@RequestParam String code) {
        var listItems = itemService.getItemStockByCode(code);

        return new ResponseEntity<>(listItems, HttpStatus.OK);
    }

    @GetMapping(value = "itemStockByName")
    public ResponseEntity<?> getItemStockByName(@RequestParam String name) {
        var listItems = itemService.getItemStockByName(name);

        return new ResponseEntity<>(listItems, HttpStatus.OK);
    }

    @GetMapping("getAllAgregated")

    public ResponseEntity<List<ItemAgregadoResponseDTO>> getAllItems(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "quantidade", defaultValue = "10") int quantidade) {

        List<ItemAgregadoResponseDTO> items = itemAgregadoService.getAllItemsPaged(page, quantidade);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<?> addItem(@RequestBody ItemDTO dto) {
        var item = Item.itemDTOToItem(dto);
        var savedItem = itemService.registerNewItem(item);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @PostMapping(value = "addStockByName/")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<?> addItemStockByName(@RequestBody ItemAddStockDTO dto) {
        var savedItem = itemService.addItemStock(dto);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "deleteByBatch")
    @PreAuthorize("hasAuthority('SCOPE_MANAGER') || hasAuthority('SCOPE_INTERN')")
    public ResponseEntity<?> deleteItemByBatch(@RequestParam String batch) {
        var isExcluded = itemService.deleteItemStockByBatch(batch);

        return new ResponseEntity<>(isExcluded, HttpStatus.OK);
    }

}
