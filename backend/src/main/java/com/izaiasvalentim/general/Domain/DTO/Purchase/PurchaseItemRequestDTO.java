package com.izaiasvalentim.general.Domain.DTO.Purchase;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PurchaseItemRequestDTO {

    @NotNull(message = "O ID do recurso e obrigatorio para cada item.")
    private Long resourceId;
    private String code;
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1.")
    private long quantity;

    // Getters e Setters
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
