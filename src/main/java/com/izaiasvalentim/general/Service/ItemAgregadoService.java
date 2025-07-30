package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceAlreadyExistsException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemAgregadoResponseDTO;
import com.izaiasvalentim.general.Domain.Item;
import com.izaiasvalentim.general.Domain.ItemAgregado;
import com.izaiasvalentim.general.Repository.ItemAgregadoRepository;
import com.izaiasvalentim.general.Repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemAgregadoService {

    private final ItemAgregadoRepository resourceRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemAgregadoService(ItemAgregadoRepository resourceRepository, ItemRepository itemRepository) {
        this.resourceRepository = resourceRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void createResourceAfterInitialItem(Item item) {
        try {
            if (itemRepository.findFirstByName(item.getName()).isEmpty()) {
                throw new ResourceAlreadyExistsException("Item com nome " + item.getName() + " não existe. " +
                        "A mercadoria não pode ser registrada.");
            }

            ItemAgregado newItemAgregado = new ItemAgregado();
            newItemAgregado.setItems(List.of(item));
            newItemAgregado.setItemCode();
            newItemAgregado.setName();
            newItemAgregado.setStock(item.getQuantity());

            var savedResource = resourceRepository.save(newItemAgregado);
            item.setItemAgregado(savedResource);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Erro ao tentar criar mercadoria do item. " + e.getMessage());
        }
    }
    @Modifying
    @Transactional
    public void updateResourceAfterChangedItemStock(Item item) {
        try {
            var resourceToUpdate = resourceRepository.findByItemCode(item.getCode()).orElse(null);
            if (resourceToUpdate == null) {
                throw new ResourceNotFoundException("Mercadoria com item de id " + item.getCode() + " não existe. " +
                        "A mercadoria não pode ser atualizada.");
            }

            List<Item> newListItems = setItemOnList(resourceToUpdate.getItems(), item);

            resourceToUpdate.setItems(newListItems);
            resourceToUpdate.setStock(calculateTotalStockOfItems(newListItems));

            var updatedResource = resourceRepository.save(resourceToUpdate);
            item.setItemAgregado(updatedResource);
        } catch (Exception e) {
            throw new ErrorInProcessServiceException("Error while creating resource after item. " + e.getMessage());
        }
    }

    private List<Item> setItemOnList(List<Item> itemList, Item item) {
        if (!item.isDeleted()) {
            itemList.add(item);
        } else {
            itemList.remove(item);
        }
        return itemList;
    }

    private Double calculateTotalStockOfItems(List<Item> items) {
        return items.stream().map(Item::getQuantity).reduce(0.0, Double::sum);
    }

    public List<ItemAgregadoResponseDTO> getAllItemsPaged(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ItemAgregado> itemsAgregadosPage = resourceRepository.findAll(pageable);

        return itemsAgregadosPage.stream()
                .map(itemAgregado -> new ItemAgregadoResponseDTO(
                        itemAgregado.getId(),
                        itemAgregado.getName(),
                        itemAgregado.getItems().getFirst().getPrice(),
                        itemAgregado.getItemCode(),
                        itemAgregado.getStock()
                ))
                .collect(Collectors.toList());
    }
}
