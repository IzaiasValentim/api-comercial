package com.izaiasvalentim.general.Service;

import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Domain.*;
import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseItemRequestDTO;
import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseItemResponseDTO;
import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseRequestDTO;
import com.izaiasvalentim.general.Domain.DTO.Purchase.PurchaseResponseDTO;
import com.izaiasvalentim.general.Domain.Enums.TypePurchaseStatus;
import com.izaiasvalentim.general.Repository.ProdutoRepository;
import com.izaiasvalentim.general.Repository.UsuarioApiRepository;
import com.izaiasvalentim.general.Repository.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioApiRepository usuarioApiRepository;
    private final UsuarioBaseService usuarioBaseService;
    private final ClienteService clienteService; // Usaremos o seu ClientService existente
    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository,
                        UsuarioApiRepository usuarioApiRepository,
                        ClienteService clienteService,
                        UsuarioBaseService usuarioBaseService, ProdutoService produtoService, ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.usuarioApiRepository = usuarioApiRepository;
        this.clienteService = clienteService;
        this.usuarioBaseService = usuarioBaseService;
        this.produtoService = produtoService;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PurchaseResponseDTO createPurchase(PurchaseRequestDTO purchaseRequestDTO) {
        // 1. Obter o vendedor autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // O nome de usuário (geralmente email ou username)

        UsuarioApi seller = usuarioApiRepository.findByUser(usuarioBaseService.findByUsername(currentUsername)).
                orElseThrow(() -> new ResourceNotFoundException("Vendedor autenticado não encontrado."));


        // 2. Obter o cliente
        Cliente cliente = clienteService.findByIdentificationNumber(purchaseRequestDTO.getClientIdentificationNumber());

        // 3. Inicializar a compra
        Venda venda = new Venda();
        venda.setPaymentMethod(purchaseRequestDTO.getPaymentMethod());
        venda.setSeller(seller);
        venda.setClient(cliente);
        venda.setStatus(TypePurchaseStatus.RECEIVED); // Status inicial
        venda.setRealizationDate(new Date()); // Data atual da realização
        venda.setHiredDate(new Date()); // Pode ser a mesma da realização ou ajustada
        venda.setDeleted(false);

        List<ItemCompra> itemDeVendas = new ArrayList<>();
        double totalPurchaseAmount = 0.0;

        // 4. Processar cada item da compra
        for (PurchaseItemRequestDTO itemDTO : purchaseRequestDTO.getItems()) {
            Produto itemParaVenda = produtoService.findByCode(itemDTO.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Recurso com ID " + itemDTO.getResourceId() + " não encontrado."));

            // Verificar estoque
            if (itemParaVenda.getTotalStock() < itemDTO.getQuantity()) {
                throw new ErrorInProcessServiceException("Estoque insuficiente para o recurso: " + itemParaVenda.getName() + ". Disponível: " + itemParaVenda.getTotalStock() + ", Solicitado: " + itemDTO.getQuantity());
            }

            // Assumindo que o preço do item vem do primeiro item associado ao Resource
            // Este é um ponto de atenção no seu modelo: `Item` tem `price`, `Resource` tem `stock` e uma lista de `Item`s.
            // Para uma venda, o preço deve ser estável. Vamos pegar o preço do primeiro item do recurso.
            // VOCÊ PODE PRECISAR REVISAR ESTA LÓGICA DE PREÇO SE UM RECURSO PUDER TER ITENS COM PREÇOS DIFERENTES.
            Double unitPrice;
            if (itemParaVenda.getLotes() != null && !itemParaVenda.getLotes().isEmpty()) {
                unitPrice = itemParaVenda.getLotes().getFirst().getPrice();
            } else {
                throw new ErrorInProcessServiceException("Recurso " + itemParaVenda.getName() + " não possui itens associados para determinar o preço.");
            }

            // Criar PurchaseItem
            ItemCompra itemDeVenda = new ItemCompra();
            itemDeVenda.setItem(itemParaVenda); // Item de PurchaseItem é Resource no seu modelo
            itemDeVenda.setQuantity(itemDTO.getQuantity());
            itemDeVenda.setPurchase(venda); // Linka o item à compra

            itemDeVendas.add(itemDeVenda);
            totalPurchaseAmount += unitPrice * itemDTO.getQuantity();

            // 5. Atualizar estoque do Resource
            itemParaVenda.setTotalStock(itemParaVenda.getTotalStock() - itemDTO.getQuantity());
            produtoRepository.save(itemParaVenda); // Persiste a alteração no estoque
        }

        venda.setPurchaseItems(itemDeVendas);
        venda.setTotal(totalPurchaseAmount);
        venda.setStatus(TypePurchaseStatus.COMPLETED); // Ou outro status final após sucesso

        // 6. Salvar a compra
        Venda savedVenda = vendaRepository.save(venda);

        // 7. Mapear para DTO de resposta
        List<PurchaseItemResponseDTO> responseItems = savedVenda.getPurchaseItems().stream()
                .map(pi -> new PurchaseItemResponseDTO(
                        pi.getItem().getName(),
                        pi.getItem().getCode(),
                        pi.getQuantity(),
                        // Recupere o preço unitário do item no momento da venda.
                        // Aqui, novamente, estamos assumindo o preço do primeiro item do Resource.
                        // Idealmente, PurchaseItem deveria ter um campo 'unitPriceAtSale' para evitar lookup.
                        pi.getItem().getLotes().getFirst().getPrice()
                ))
                .collect(Collectors.toList());

        return new PurchaseResponseDTO(
                savedVenda.getId(),
                savedVenda.getTotal(),
                savedVenda.getPaymentMethod(),
                savedVenda.getSeller().getUser().getUsername(), // Assumindo que BaseUser tem getUsername()
                savedVenda.getClient().getName(), // Assumindo que Client tem getName()
                savedVenda.getStatus(), // Usa o getter transient
                LocalDateTime.now(), // Ou converta Date para LocalDateTime
                responseItems
        );
    }

}
