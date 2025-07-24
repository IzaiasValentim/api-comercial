package com.izaiasvalentim.general.Domain.DTO.Client;

import com.izaiasvalentim.general.Domain.Cliente;

public class ClientRegisterDTO {
    private String name;
    private String email;
    private String identificationNumber;
    private String address;
    private String phoneNumber;
    private String phoneNumberReserve;
    private String payment;
    private String status;
    private Boolean isDeleted;

    public ClientRegisterDTO() {
    }

    public ClientRegisterDTO(String name, String email, String identificationNumber, String address, String phoneNumber
            , String phoneNumberReserve, String payment, String status) {
        this.name = name;
        this.email = email;
        this.identificationNumber = identificationNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneNumberReserve = phoneNumberReserve;
        this.payment = payment;
        this.status = status;
    }

    public ClientRegisterDTO(Cliente cliente) {
        this.name = cliente.getName();
        this.email = cliente.getEmail();
        this.identificationNumber = cliente.getIdentificationNumber();
        this.address = cliente.getAddress();
        this.phoneNumber = cliente.getPhoneNumber();
        this.phoneNumberReserve = cliente.getPhoneNumberReserve();
        this.payment = cliente.getPayment();
        if (cliente.getActive()) {
            status = "Active";
        } else {
            status = "Inactive";
        }
        this.isDeleted = cliente.getDeleted();
    }

    public Cliente registerDTOToClient() {
        return new Cliente(name, email, identificationNumber, address, phoneNumber, phoneNumberReserve, payment, isDeleted);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
