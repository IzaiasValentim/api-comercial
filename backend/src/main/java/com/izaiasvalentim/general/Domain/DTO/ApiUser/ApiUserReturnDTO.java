package com.izaiasvalentim.general.Domain.DTO.ApiUser;

import com.izaiasvalentim.general.Domain.UsuarioApi;
import com.izaiasvalentim.general.Domain.Role;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ApiUserReturnDTO {
    private String username;
    private String email;
    private String phone;
    private String address;
    private LocalDate admissionDate;
    private Boolean isAdmin;
    private Boolean isActive;
    private Boolean isDeleted;
    private Set<Role> roles = new HashSet<>();

    public ApiUserReturnDTO(UsuarioApi usuarioApi) {
        this.username = usuarioApi.getUser().getUsername();
        this.email = usuarioApi.getUser().getEmail();
        this.phone = usuarioApi.getPhone();
        this.address = usuarioApi.getAddress();
        this.admissionDate = usuarioApi.getAdmissionDate();
        this.isAdmin = usuarioApi.getAdmin();
        this.isActive = usuarioApi.getIsActive();
        this.isDeleted = usuarioApi.getDeleted();
        this.setRoles(usuarioApi.getUser().getRoles());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
