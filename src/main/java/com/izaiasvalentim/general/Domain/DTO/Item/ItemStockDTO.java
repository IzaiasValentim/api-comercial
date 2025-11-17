package com.izaiasvalentim.general.Domain.DTO.Item;

import com.izaiasvalentim.general.Domain.Item;

import java.util.List;

public class ItemStockDTO {
    private Long id;
    private String nome;
    private Double price;
    private String itemCode;
    private Double stock;
    private List<Item> itens;

    public ItemStockDTO(Long id, String nome, Double price, String itemCode, Double stock, List<Item> itens) {
        this.id = id;
        this.nome = nome;
        this.price = price;
        this.itemCode = itemCode;
        this.stock = stock;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
