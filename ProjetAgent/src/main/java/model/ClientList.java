package model;

import java.util.Iterator;
import java.util.List;

public class ClientList implements java.io.Serializable {
    // --------------------------------------------------
    private List clients;


    public List getClients() {
        return clients;
    }


    public void setClients(List clients) {
        this.clients = clients;
    }

    public String toString() {
        String s = "\n\tLIST OF ORDERS:";
        for (Iterator it = clients.iterator(); it.hasNext();) {
            Client op = (Client) it.next();
            s += op.toString();
        }
        return s;
    }
}