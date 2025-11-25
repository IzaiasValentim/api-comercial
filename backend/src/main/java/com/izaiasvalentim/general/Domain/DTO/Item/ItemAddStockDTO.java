package com.izaiasvalentim.general.Domain.DTO.Item;

import java.util.Date;

public class ItemAddStockDTO {
    private String code;
    private Double price;
    private Double quantity;
    private Date validity;
    private Boolean hasValidity;

    public ItemAddStockDTO() {
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getHasValidity() {
        return hasValidity;
    }

    public void setHasValidity(Boolean hasValidity) {
        this.hasValidity = hasValidity;
    }
}
