
package com.inventapi.demo;

import javax.persistence.*;

@Entity
public class Product implements java.io.Serializable {

    public Product() {};
    public Product(String nom, int price, int quantity) {
        super();
        this.nom = nom;
        this.price = price;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name="nom")
    String nom;
    @Column(name="price")
    int price;
    @Column(name="quantity")
    int quantity;
    @Column(name="categorie")
    String categorie;
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
