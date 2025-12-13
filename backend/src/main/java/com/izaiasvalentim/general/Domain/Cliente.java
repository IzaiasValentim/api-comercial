package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String identificationNumber;

    @Column(nullable = false)
    private String address;

    private String phoneNumber;

    private String phoneNumberReserve;

    @Column(nullable = false)
    private String payment;

    /*
    * TODO
    *
    * REFATORAR E DEIXAR A ASSOCIAÇÃO DE VENDA PARA CLIENTE
    *
    * REMOVER AQUIII
    *
    * */
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Venda> allPurchases;

    private Boolean active;

    private Boolean isDeleted;

    public Cliente() {

    }

    public Cliente(String name, String email, String identificationNumber, String address, String phoneNumber,
                   String phoneNumberReserve, String payment, Boolean isDeleted) {
        this.name = name;
        this.email = email;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneNumberReserve = phoneNumberReserve;
        this.payment = payment;
        this.active = false;
        this.isDeleted = isDeleted;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumberReserve() {
        return phoneNumberReserve;
    }

    public void setPhoneNumberReserve(String phoneNumberReserve) {
        this.phoneNumberReserve = phoneNumberReserve;
    }

    public List<Venda> getAllPurchases() {
        return allPurchases;
    }

    public void setAllPurchases(List<Venda> allPurchases) {
        this.allPurchases = allPurchases;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
