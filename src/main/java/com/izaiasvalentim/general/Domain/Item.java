package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemAddStockDTO;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private Double price;

    private Double quantity;

    @Column(unique = true)
    private String batch;

    @Temporal(TemporalType.TIMESTAMP)
    private Date validity;

    private Boolean hasValidity;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonBackReference
    private Produto produto;

    public Item() {
    }

    public Item(ItemAddStockDTO dto, Produto produto) {
        this.price = dto.getPrice();
        this.quantity = dto.getQuantity();
        this.validity = dto.getValidity();
        this.hasValidity = dto.getHasValidity();
        this.deleted = false;
        this.produto = produto;
    }

    public Item(ItemDTO itemDto) {
        this.price = itemDto.price();
        this.quantity = itemDto.quantity();
        this.validity = itemDto.validity() ;
        this.hasValidity = itemDto.hasValidity();
        this.deleted = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
