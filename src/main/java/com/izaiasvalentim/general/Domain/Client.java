package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Clients")
public class Client {
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
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Purchase> allPurchases;
    private Boolean active;
    private Boolean isDeleted;

    public Client() {

    }

    public Client(String name, String email, String identificationNumber, String address, String phoneNumber,
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<Purchase> getAllPurchases() {
        return allPurchases;
    }

    public void setAllPurchases(List<Purchase> allPurchases) {
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
