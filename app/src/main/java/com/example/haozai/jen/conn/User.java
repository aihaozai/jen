package com.example.haozai.jen.conn;

public class User {
    private String username;
    private String password;
    private int sid;
    private Float money;
    private String pswd;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public Float getMoney() {
        return money;
    }
    public void setMoney(Float money) {
        this.money = money;
    }
    public String getPswd() {
        return pswd;
    }
    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", sid=" + sid + ", money=" + money + ", pswd="
                + pswd + "]";
    }
}
