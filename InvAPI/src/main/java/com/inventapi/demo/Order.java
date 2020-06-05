package com.inventapi.demo;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name ="clientorder")
public class Order implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Temporal(TemporalType.DATE)
    Date date;
    @ManyToOne
    @JoinColumn(name ="CLIENT_ID")
    Client client;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "order")
    List<Produit> produits;

    public Order() {};

    public Order(Date date, String nom, String prenom, String email) {
        super();
        this.date = date;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public List<Produit> getProduits() {
        return produits;
    }
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }




}