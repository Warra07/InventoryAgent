package agent;
public interface AgentVocabulary {

    public static final String SERVER_AGENT = "Server agent";
    public static final String PB_CLIENT_NOT_FOUND = "Client not found";
    public static final String PB_ORDER_NOT_FOUND = "Order not found";
    public static final String PB_PRDUIT_NOT_FOUND = "Produit not found";
    public static final String PB_ILLEGAL_OPERATION = "Illegal operation";


    public static final int CLIENT = 1;
    public static final int PRODUIT = 2;
    public static final int ORDER = 3;
    public static final int CLIENTS = 4;
    public static final int NEW_PRODUCT = 5;
    public static final int NEW_ORDER = 6;
    public static final int NEW_CLIENT = 7;


    public static final int PRODUIT_NOT_FOUND = 8;
    public static final int ILLEGAL_OPERATION = 9;
    public static final int CLIENT_NOT_FOUND = 10;
    public static final int ORDER_NOT_FOUND = 11;
}