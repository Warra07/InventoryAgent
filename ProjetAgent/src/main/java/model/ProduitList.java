package model;

import java.util.Iterator;
import java.util.List;

public class ProduitList implements java.io.Serializable {

private List produits;


public List getProduits() {
        return produits;
        }

public void setProduits(List produits) {
        this.produits = produits;
        }

public String toString() {
        String s = "\n\tLIST OF PRODUITS:";
        for (Iterator it = produits.iterator(); it.hasNext();) {
        Produit op = (Produit)it.next();
        s += op.toString();
        }
        return s;
        }
        }