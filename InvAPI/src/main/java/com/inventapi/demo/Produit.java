package com.inventapi.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class Produit implements java.io.Serializable {

    public Produit() {};
    public Produit(String nom, int price, int quantity, String categorie) {
        super();
        this.nom = nom;
        this.price = price;
        this.quantity = quantity;
        this.categorie = categorie;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String nom;
    @Column
    int price;
    @Column
    int quantity;
    @Column
    String categorie;
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Order order;




    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getnom() {
        return nom;
    }
    public void setnom(String nom) {
        this.nom = nom;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getCategorie() {
        return categorie;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }


}
