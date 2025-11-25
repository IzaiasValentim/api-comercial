package com.izaiasvalentim.general.Domain.DTO.Purchase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PurchaseResponseDTO {
    private UUID id;
    private Double total;
    private String paymentMethod;
    private String sellerUsername; // Ou o nome do vendedor
    private String clientName;     // Ou o nome do cliente
    private String status;
    private LocalDateTime realizationDate;
    private List<PurchaseItemResponseDTO> items;

    // Construtor a partir da entidade Purchase
    public PurchaseResponseDTO(UUID id, Double total, String paymentMethod, String sellerUsername, String clientName, String status, LocalDateTime realizationDate, List<PurchaseItemResponseDTO> items) {
        this.id = id;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.sellerUsername = sellerUsername;
        this.clientName = clientName;
        this.status = status;
        this.realizationDate = realizationDate;
        this.items = items;
    }

    // Getters (Setters podem ser omitidos se o DTO for imutável para a resposta)
    public UUID getId() {
        return id;
    }

    public Double getTotal() {
        return total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getClientName() {
        return clientName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getRealizationDate() {
        return realizationDate;
    }

    public List<PurchaseItemResponseDTO> getItems() {
        return items;
    }
}
