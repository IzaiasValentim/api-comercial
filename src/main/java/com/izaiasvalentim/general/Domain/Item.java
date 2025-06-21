package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.izaiasvalentim.general.Domain.DTO.Item.ItemDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String name;
    private String type;
    private Double price;
    private Double quantity;
    @Column(unique = true, nullable = true)
    private String batch;
    private String code;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validity;
    private Boolean hasValidity;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "resource_id")
    @JsonBackReference
    private Resource resource;

    public Item(String name, String type, Double price, Double quantity, String batch, String code,
                Date validity, Boolean hasValidity, boolean isDeleted) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.batch = batch;
        this.code = code;
        this.validity = validity;
        this.hasValidity = hasValidity;
        this.isDeleted = isDeleted;
    }

    public Item() {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public static Item itemDTOToItem(ItemDTO dto) {
        return new Item(dto.name(), dto.type(), dto.price(), dto.quantity(), null, dto.code(), dto.validity(),
                dto.hasValidity(), false);
    }
}
