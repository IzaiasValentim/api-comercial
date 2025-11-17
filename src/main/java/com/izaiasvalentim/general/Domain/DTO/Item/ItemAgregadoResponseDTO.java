package com.izaiasvalentim.general.Domain.DTO.Item;

public class ItemAgregadoResponseDTO {
    private Long id;
    private String nome;
    private Double price;
    private String itemCode;
    private Double stock;

    public ItemAgregadoResponseDTO(Long id, String nome, Double price, String itemCode, Double stock) {
        System.out.println("Fui criado");
        this.id = id;
        this.nome = nome;
        this.price = price;
        this.itemCode = itemCode;
        this.stock = stock;
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
}
