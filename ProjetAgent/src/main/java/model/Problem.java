package model;
public class Problem implements java.io.Serializable {
// --------------------------------------------

    private int num;
    private String msg;

    public int getNum() {
        return this.num;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}