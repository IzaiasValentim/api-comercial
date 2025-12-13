package com.izaiasvalentim.general.Service;


import com.izaiasvalentim.general.Common.CustomExceptions.ErrorInProcessServiceException;
import com.izaiasvalentim.general.Common.CustomExceptions.ResourceNotFoundException;
import com.izaiasvalentim.general.Common.utils.ItemUtils;
import com.izaiasvalentim.general.Domain.*;
import com.izaiasvalentim.general.Domain.DTO.Purchase.*;
import com.izaiasvalentim.general.Domain.Enums.TypePurchaseStatus;
import com.izaiasvalentim.general.Repository.ItemRepository;
import com.izaiasvalentim.general.Repository.ProdutoRepository;
import com.izaiasvalentim.general.Repository.UsuarioApiRepository;
import com.izaiasvalentim.general.Repository.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final UsuarioApiRepository usuarioApiRepository;
    private final UsuarioBaseService usuarioBaseService;
    private final ClienteService clienteService;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;

    public VendaService(VendaRepository vendaRepository,
                        UsuarioApiRepository usuarioApiRepository,
                        ClienteService clienteService,
                        UsuarioBaseService usuarioBaseService,
                        ProdutoRepository produtoRepository,
                        ItemRepository itemRepository) {
        this.vendaRepository = vendaRepository;
        this.usuarioApiRepository = usuarioApiRepository;
        this.clienteService = clienteService;
        this.usuarioBaseService = usuarioBaseService;
        this.produtoRepository = produtoRepository;
        this.itemRepository = itemRepository;
    }

    public PurchaseResponseDTO buscarVendaPorId(UUID idVenda) {
       Venda buscaVenda =  vendaRepository.findByIdAndIsDeleted(idVenda, false)
                .orElseThrow(() -> new ResourceNotFoundException("Não encontramos venda cadastrada com esse id"));
        return toResponseDTO(buscaVenda);
    }

    public Page<PurchaseListDTO> findAllPaged(String cpf, String name, Integer statusStr, int page, int size) {
        Integer statusId = null;
        System.out.println("\n\n\n\n dsadsadsadasds"+statusStr);
        if (statusStr != null) {
            try {
                TypePurchaseStatus time = TypePurchaseStatus.getStatusEnumById(statusStr);
                statusId = time.getId();
            } catch (IllegalArgumentException e) {
                statusId = null;
            }
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("realizationDate").descending());

        return vendaRepository.findByFilters(cpf, name, statusId, pageRequest)
                .map(PurchaseListDTO::new);
    }

    @Transactional
    public PurchaseResponseDTO createPurchase(PurchaseRequestDTO dto) {
        UsuarioApi seller = getAuthenticatedSeller();
        Cliente cliente = clienteService.findByIdentificationNumber(dto.getClientIdentificationNumber());

        Venda venda = new Venda();
        venda.setSeller(seller);
        venda.setClient(cliente);
        venda.setPaymentMethod(dto.getPaymentMethod());
        venda.setStatus(TypePurchaseStatus.RECEIVED);
        venda.setRealizationDate(new Date());
        venda.setDeleted(false);

        List<ItemCompra> itensCompra = processarItensVenda(dto.getItems(), venda);

        venda.setPurchaseItems(itensCompra);
        venda.setTotal(calcularTotal(itensCompra));

        vendaRepository.save(venda);
        return toResponseDTO(venda);
    }

    @Transactional
    public PurchaseResponseDTO updatePurchase(UUID id, VendaUpdateDTO dto) {
        Venda venda = vendaRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));

        if (venda.getStatus().equals(TypePurchaseStatus.CANCELLED.getStatus()) ||
                venda.getStatus().equals(TypePurchaseStatus.COMPLETED.getStatus())) {
            throw new ErrorInProcessServiceException("Não é possível alterar vendas Canceladas ou Concluídas.");
        }

        devolverEstoque(venda.getPurchaseItems());

        venda.getPurchaseItems().clear();

        if (dto.paymentMethod() != null) venda.setPaymentMethod(dto.paymentMethod());

        if (dto.items() != null && !dto.items().isEmpty()) {
            List<ItemCompra> novosItens = processarItensVenda(dto.items(), venda);
            venda.getPurchaseItems().addAll(novosItens);
            venda.setTotal(calcularTotal(novosItens));
        }

        vendaRepository.save(venda);
        return toResponseDTO(venda);
    }

    @Transactional
    public void updateStatus(UUID id, VendaStatusDTO dto) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));

        if (venda.getStatus().equals(TypePurchaseStatus.CANCELLED.getStatus())) {
            throw new ErrorInProcessServiceException("Venda cancelada não pode mudar de status.");
        }

        TypePurchaseStatus status = TypePurchaseStatus.getStatusEnumById(dto.status());
            if(status != null) {
                if (status.equals(TypePurchaseStatus.CANCELLED)) {
                    cancelPurchase(id);
                    return;
                }
                venda.setStatus(status);
                vendaRepository.save(venda);
            }
    }

    @Transactional
    public void cancelPurchase(UUID id) {
        Venda venda = vendaRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada."));

        if (venda.getStatus().equals(TypePurchaseStatus.CANCELLED.getStatus())) {
            throw new ErrorInProcessServiceException("Venda já está cancelada.");
        }

        devolverEstoque(venda.getPurchaseItems());

        venda.setStatus(TypePurchaseStatus.CANCELLED);
        vendaRepository.save(venda);
    }

    private List<ItemCompra> processarItensVenda(List<PurchaseItemRequestDTO> itensSolicitados, Venda venda) {
        List<ItemCompra> resultado = new ArrayList<>();

        for (PurchaseItemRequestDTO solicitacao : itensSolicitados) {
            Produto produto = produtoRepository.findByCode(solicitacao.getCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + solicitacao.getCode()));

            if (produto.getTotalStock() < solicitacao.getQuantity()) {
                throw new ErrorInProcessServiceException("Estoque insuficiente para: " + produto.getName());
            }


            double quantidadeRestanteParaBaixar = solicitacao.getQuantity();
            double custoTotalDesteItem = 0.0;

            List<Item> lotesDisponiveis = produto.getLotes().stream()
                    .filter(l -> !Boolean.TRUE.equals(l.getDeleted()) && l.getQuantity() > 0)
                    .sorted(Comparator.comparing(Item::getId)) // Ou getValidity se preferir
                    .toList();

            for (Item lote : lotesDisponiveis) {
                if (quantidadeRestanteParaBaixar <= 0) break;

                double qtdBaixarNesteLote = Math.min(quantidadeRestanteParaBaixar, lote.getQuantity());


                lote.setQuantity(lote.getQuantity() - qtdBaixarNesteLote);
                if (lote.getQuantity() == 0) {
                    lote.setDeleted(true);
                }
                itemRepository.save(lote);


                custoTotalDesteItem += (qtdBaixarNesteLote * lote.getPrice());
                quantidadeRestanteParaBaixar -= qtdBaixarNesteLote;
            }

            produto.calculateTotalStock();
            produtoRepository.save(produto);

            ItemCompra itemCompra = new ItemCompra();
            itemCompra.setItem(produto);
            itemCompra.setQuantity(solicitacao.getQuantity());
            itemCompra.setPurchase(venda);

            resultado.add(itemCompra);
        }
        return resultado;
    }


    private void devolverEstoque(List<ItemCompra> itensParaDevolver) {
        for (ItemCompra ic : itensParaDevolver) {
            Produto produto = ic.getItem();

            Optional<Item> loteAtivo = produto.getLotes().stream()
                    .filter(l -> !Boolean.TRUE.equals(l.getDeleted()))
                    .findFirst();

            if (loteAtivo.isPresent()) {
                Item lote = loteAtivo.get();
                lote.setQuantity(lote.getQuantity() + ic.getQuantity());
                itemRepository.save(lote);
            } else {
                Item novoLote = new Item();
                novoLote.setProduto(produto);
                novoLote.setQuantity((double) ic.getQuantity());
                novoLote.setPrice(0.0);
                novoLote.setDeleted(false);
                ItemUtils.generateItemBatch(produto, novoLote);
                produto.getLotes().add(novoLote);
            }

            produto.calculateTotalStock();
            produtoRepository.save(produto);
        }
    }

    private Double calcularTotal(List<ItemCompra> itens) {
        return itens.stream()
                .mapToDouble(i -> {
                    double precoUnitario = i.getItem().getLotes().stream()
                            .filter(l -> l.getQuantity() > 0)
                            .findFirst()
                            .map(Item::getPrice)
                            .orElse(0.0);
                    return precoUnitario * i.getQuantity();
                })
                .sum();
    }

    private UsuarioApi getAuthenticatedSeller() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioApiRepository.findByUser(usuarioBaseService.findByUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado."));
    }

    private PurchaseResponseDTO toResponseDTO(Venda venda) {
        List<PurchaseItemResponseDTO> itemsDTO = venda.getPurchaseItems().stream()
                .map(i -> new PurchaseItemResponseDTO(
                        i.getItem().getName(),
                        i.getItem().getCode(),
                        i.getQuantity(),
                        i.getItem().getLotes().isEmpty() ? 0.0 : i.getItem().getLotes().getFirst().getPrice()
                ))
                .collect(Collectors.toList());

        return new PurchaseResponseDTO(
                venda.getId(),
                venda.getTotal(),
                venda.getPaymentMethod(),
                venda.getSeller().getUser().getUsername(),
                venda.getClient().getName(),
                venda.getStatus(),
                venda.getRealizationDate() != null ? LocalDateTime.now() : null,
                itemsDTO
        );
    }
}
