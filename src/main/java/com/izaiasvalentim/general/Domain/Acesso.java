package com.izaiasvalentim.general.Domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    @JoinColumn(name = "usuarioApi_id")
    private UsuarioApi user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Acesso(UsuarioApi user, Date date) {
        this.user = user;
        this.date = date;
    }

    public Acesso() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioApi getUser() {
        return user;
    }

    public void setUser(UsuarioApi user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
