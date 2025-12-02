package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.izaiasvalentim.general.Domain.Enums.TypePurchaseStatus;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double total;

    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "usuarioApi_id", referencedColumnName = "id")
    private UsuarioApi seller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @JsonBackReference
    private Cliente cliente;

    @Column(name = "status_id")
    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date realizationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date hiredDate;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCompra> itemCompras;

    private int statusCompraId;

    public Venda(Double total, String paymentMethod, UsuarioApi seller, Cliente cliente, int status,
                 Date realizationDate, Date hiredDate, Boolean isDeleted) {
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.seller = seller;
        this.cliente = cliente;
        this.status = status;
        this.realizationDate = realizationDate;
        this.hiredDate = hiredDate;
        this.isDeleted = isDeleted;
    }

    public Venda() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UsuarioApi getSeller() {
        return seller;
    }

    public void setSeller(UsuarioApi seller) {
        this.seller = seller;
    }

    public Cliente getClient() {
        return cliente;
    }

    public void setClient(Cliente cliente) {
        this.cliente = cliente;
    }

    @Transient
    public String getStatus() {
        return TypePurchaseStatus.getStatusById(this.status);
    }

    public void setStatus(TypePurchaseStatus status) {
        if (status == null) {
            this.status = 0;
        } else {
            this.status = status.getId();
            this.statusCompraId = status.getId();
        }
    }
    public Date getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(Date efetivatedDate) {
        this.hiredDate = efetivatedDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<ItemCompra> getPurchaseItems() {
        return itemCompras;
    }

    public void setPurchaseItems(List<ItemCompra> itemCompras) {
        this.itemCompras = itemCompras;
    }

    public Date getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(Date realizationDate) {
        this.realizationDate = realizationDate;
    }

    public int getStatusCompraId() {
        return statusCompraId;
    }

    public void setStatusCompraId(int statusCompraId) {
        if (this.getStatus() == null) {
            this.statusCompraId = 0;
        } else {
            this.statusCompraId = this.status;
        }
    }
}