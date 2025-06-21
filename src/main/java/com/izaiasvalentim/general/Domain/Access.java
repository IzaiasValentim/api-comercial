package com.izaiasvalentim.general.Domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @OneToOne
    @JoinColumn(name = "apiUser_id")
    private ApiUser user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Access(ApiUser user, Date date) {
        this.user = user;
        this.date = date;
    }

    public Access() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApiUser getUser() {
        return user;
    }

    public void setUser(ApiUser user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
