package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false, name = "item_name")
    private String name;
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Item> items;
    private String itemCode;
    private Double stock;

    public Resource(List<Item> items, Double stock) {
        this.items = items;
        this.stock = stock;
    }

    public Resource() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        if (!this.items.isEmpty()) {
            this.name = this.items.getFirst().getName();

        } else {
            // É para gerar uma exception personalizada.
            this.name = "";
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode() {
        if (!this.items.isEmpty()) {
            this.itemCode = this.items.getFirst().getCode();
        } else {
            // É para gerar uma exception personalizada.
            this.itemCode = "";
        }
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }
}


