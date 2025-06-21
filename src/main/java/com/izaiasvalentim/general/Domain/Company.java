package com.izaiasvalentim.general.Domain;

import jakarta.persistence.*;

@Entity
public class Company {
    @Id
    private int id;
    @Column(unique = true)
    private String name;
    private String brach;
    private String address;
    private String typeOfService;
    public Company(String name, String brach, String address, String typeOfService) {
        this.name = name;
        this.brach = brach;
        this.address = address;
        this.typeOfService = typeOfService;
    }

    public Company() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrach() {
        return brach;
    }

    public void setBrach(String brach) {
        this.brach = brach;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }
}
