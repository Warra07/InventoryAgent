package model;

import java.util.Iterator;
import java.util.List;

public class OrderList implements java.io.Serializable {
// --------------------------------------------------
    private List orders;


    public List getOrders() {
        return orders;
    }


    public void setOrders(List orders) {
        this.orders = orders;
    }

    public String toString() {
        String s = "\n\tLIST OF ORDERS:";
        for (Iterator it = orders.iterator(); it.hasNext();) {
            Order op = (Order) it.next();
            s += op.toString();
        }
        return s;
    }
}