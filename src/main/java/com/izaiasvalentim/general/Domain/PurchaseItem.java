package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private Resource item;
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    @JsonBackReference
    private Purchase purchase;
    private long quantity;

    public PurchaseItem(Resource item, long quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public PurchaseItem() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Resource getItem() {
        return item;
    }

    public void setItem(Resource item) {
        this.item = item;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}

