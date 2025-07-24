package com.izaiasvalentim.general.Domain.DTO.Purchase;

public class PurchaseItemResponseDTO {
    private String itemName;
    private String itemCode;
    private long quantity;
    private Double unitPrice; // Preco do item no momento da venda

    public PurchaseItemResponseDTO(String itemName, String itemCode, long quantity, Double unitPrice) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public long getQuantity() {
        return quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }
}
