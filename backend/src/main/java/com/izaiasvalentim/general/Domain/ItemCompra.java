package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ItemCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "produto_id")
    private Produto item;

    @ManyToOne
    @JoinColumn(name = "compra_id", referencedColumnName = "id")
    @JsonBackReference
    private Venda venda;

    private long quantity;

    public ItemCompra(Produto item, long quantity) {
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

    public Produto getItem() {
        return item;
    }

    public void setItem(Produto item) {
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

