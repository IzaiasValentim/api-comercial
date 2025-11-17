package com.izaiasvalentim.general.Domain;

import com.izaiasvalentim.general.Domain.Enums.TypeRoles;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "USUARIO_API")
public class UsuarioApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UsuarioBase user;
    private String phone;
    private String address;
    @Temporal(TemporalType.DATE)
    private LocalDate admissionDate;
    @Temporal(TemporalType.DATE)
    private LocalDate shutdowsDate;
    private Boolean isAdmin;
    private Boolean isActive;
    private Boolean isDeleted;

    public UsuarioApi(String email, String username, String CPF, String phone, String address,
                      LocalDate admissionDate, LocalDate shutdowsDate, Boolean isActive, Boolean isDeleted) {
        this.phone = phone;
        this.address = address;
        this.admissionDate = admissionDate;
        this.shutdowsDate = shutdowsDate;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

    public UsuarioApi() {
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioBase getUser() {
        return user;
    }

    public void setUser(UsuarioBase user) {
        this.user = user;
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

    public LocalDate getShutdowsDate() {
        return shutdowsDate;
    }

    public void setShutdowsDate(LocalDate shutdowsDate) {
        this.shutdowsDate = shutdowsDate;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setIsAdmin() {
        for (Role role : this.user.getRoles()) {
            if (role.getId() == TypeRoles.ADMIN.getId()) {
                isAdmin = true;
                break;
            }
        }
        isAdmin = false;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
