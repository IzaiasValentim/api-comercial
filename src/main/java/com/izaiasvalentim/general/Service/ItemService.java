package com.izaiasvalentim.general.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Common.utils.ItemUtils;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemAddStockDTO;
import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Repository.ItemRepository;

import jakarta.transaction.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemAgregadoService itemAgregadoService;


    @Autowired
    public ItemService(ItemRepository itemRepository, ItemAgregadoService itemAgregadoService) {
        this.itemRepository = itemRepository;
        this.itemAgregadoService = itemAgregadoService;
    }

    @Transactional
    public Item registerNewItem(Item item) {
        try {
            if (itemRepository.findFirstByName(item.getName()).isPresent()) {
                throw new ResourceAlreadyExistsException("Item de nome " + item.getName() + " já existe. " +
                        "Tente adicionar estoque para ele.");
            }
            item.setDeleted(false);
            var savedItem = itemRepository.save(item);
            ItemUtils.generateItemBatch(savedItem);

            itemAgregadoService.createResourceAfterInitialItem(savedItem);

            itemRepository.save(savedItem);

            return savedItem;
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    @Transactional
    public Item addItemStock(ItemAddStockDTO itemDTO) {
        try {
            Item validateItem = itemRepository.findFirstByName(itemDTO.getName()).orElse(null);

            if (validateItem != null) {

                Item newItem = new Item();
                newItem.setName(validateItem.getName());
                newItem.setType(validateItem.getType());
                newItem.setPrice(itemDTO.getPrice());
                newItem.setQuantity(itemDTO.getQuantity());
                newItem.setCode(validateItem.getCode());
                newItem.setValidity(itemDTO.getValidity());
                newItem.setHasValidity(itemDTO.getHasValidity());

                var savedItem = itemRepository.save(newItem);
                ItemUtils.generateItemBatch(savedItem);

                itemAgregadoService.updateResourceAfterChangedItemStock(savedItem);

                itemRepository.save(savedItem);

                return savedItem;
            } else {
                throw new ResourceNotFoundException("Item com nome " + itemDTO.getName() + " não existe.");
            }
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }
    }

    public List<Item> getAllItemsByName(String name) {
        return itemRepository.findAllByName(name).orElse(List.of());
    }

    @Transactional
    public boolean deleteItemStockByBatch(String batch) {
        try {
            var batchToDelete = itemRepository.findByBatch(batch).orElse(null);
            if (batchToDelete != null) {
                batchToDelete.setDeleted(true);
                itemAgregadoService.updateResourceAfterChangedItemStock(batchToDelete);
                itemRepository.delete(batchToDelete);
                return true;
            } else {
                throw new ResourceNotFoundException("Item com nome " + batch + " não existe.");
            }
        } catch (Exception e) {
            throw new ErrorInProcessServiceException(e.getMessage());
        }

    }

}
