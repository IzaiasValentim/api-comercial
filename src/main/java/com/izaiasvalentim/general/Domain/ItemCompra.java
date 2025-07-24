package com.izaiasvalentim.general.Domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class ItemCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itemAgregado_id")
    private ItemAgregado item;
    @ManyToOne
    @JoinColumn(name = "compra_id", referencedColumnName = "id")
    @JsonBackReference
    private Venda venda;
    private long quantity;

    public ItemCompra(ItemAgregado item, long quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public ItemCompra() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ItemAgregado getItem() {
        return item;
    }

    public void setItem(ItemAgregado item) {
        this.item = item;
    }

    public Venda getPurchase() {
        return venda;
    }

    public void setPurchase(Venda venda) {
        this.venda = venda;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

