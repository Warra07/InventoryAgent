package model;


public class Information implements java.io.Serializable {
// ------------------------------------------------

    private int type;
    private Long clientId;

    public int getType() {
        return type;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}