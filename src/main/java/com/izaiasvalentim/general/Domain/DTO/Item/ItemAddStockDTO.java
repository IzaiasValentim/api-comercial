package com.izaiasvalentim.general.Domain.DTO.Item;

import java.util.Date;

public class ItemAddStockDTO {
    private String name;
    private Double price;
    private Double quantity;
    private Date validity;
    private Boolean hasValidity;

    public ItemAddStockDTO(String name, Double price, Double quantity, Date validity, Boolean hasValidity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.validity = validity;
        this.hasValidity = hasValidity;
    }

    public ItemAddStockDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public Boolean getHasValidity() {
        return hasValidity;
    }

    public void setHasValidity(Boolean hasValidity) {
        this.hasValidity = hasValidity;
    }
}
