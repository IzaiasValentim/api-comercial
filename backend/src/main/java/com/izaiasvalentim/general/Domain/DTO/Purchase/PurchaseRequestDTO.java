package com.izaiasvalentim.general.Domain.DTO.Purchase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PurchaseRequestDTO {

    @NotBlank(message = "O método de pagamento é obrigatório.")
    private String paymentMethod;

    @NotBlank(message = "O CPF/CNPJ do cliente é obrigatório.")
    private String clientIdentificationNumber;

    @NotEmpty(message = "A venda deve conter pelo menos um item.")
    @Size(min = 1, message = "A venda deve conter pelo menos um item.")
    @Valid // Para validar os PurchaseItemRequestDTOs dentro da lista
    private List<PurchaseItemRequestDTO> items;

    // Getters e Setters
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getClientIdentificationNumber() {
        return clientIdentificationNumber;
    }

    public void setClientIdentificationNumber(String clientIdentificationNumber) {
        this.clientIdentificationNumber = clientIdentificationNumber;
    }

    public List<PurchaseItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemRequestDTO> items) {
        this.items = items;
    }
}
