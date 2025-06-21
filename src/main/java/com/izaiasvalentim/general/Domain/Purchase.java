package com.izaiasvalentim.general.Domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.izaiasvalentim.general.Domain.Enums.TypePurchaseStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Double total;
    private String paymentMethod;
    @OneToOne
    @JoinColumn(name = "apiUser_id")
    private ApiUser seller;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id", referencedColumnName = "id")
    @JsonBackReference
    private Client client;
    @Column(name = "status_id")
    private int status;
    @Temporal(TemporalType.TIMESTAMP)
    private Date realizationDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date hiredDate;
    private Boolean isDeleted;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PurchaseItem> purchaseItems;

    public Purchase(Double total, String paymentMethod, ApiUser seller, Client client, int status,
                    Date realizationDate, Date hiredDate, Boolean isDeleted) {
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.seller = seller;
        this.client = client;
        this.status = status;
        this.realizationDate = realizationDate;
        this.hiredDate = hiredDate;
        this.isDeleted = isDeleted;
    }

    public Purchase() {
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

    public ApiUser getSeller() {
        return seller;
    }

    public void setSeller(ApiUser seller) {
        this.seller = seller;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        }
    }

    public Date getRealizationDate() {
        return realizationDate;
    }

    public void setRealizationDate(Date realizationDate) {
        this.realizationDate = realizationDate;
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

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}