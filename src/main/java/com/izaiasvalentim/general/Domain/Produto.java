package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    private String category;

    private Double totalStock;

    private Boolean active = true;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> lotes = new ArrayList<>();

    public Produto() {
    }

    public Produto(ItemDTO dto) {
        this.name = dto.name();
        this.code = dto.code();
        this.category = dto.type();
        this.active = true;
        this.totalStock = 0.0;
    }

    public void calculateTotalStock() {
        this.totalStock = this.lotes.stream()
                .filter(item -> !Boolean.TRUE.equals(item.getDeleted())) // Ignora deletados
                .mapToDouble(Item::getQuantity)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Double totalStock) {
        this.totalStock = totalStock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Item> getLotes() {
        return lotes;
    }

    public void setLotes(List<Item> lotes) {
        this.lotes = lotes;
    }
}
