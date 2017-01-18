package com.example.shan.location;

/**
 * Created by chamod on 1/15/17.
 */

public class User {
    private String username;
    private String password;
    private String emi;

    public User(String username,String emi, String password) {
        this.username = username;
        this.password = password;
        this.emi=emi;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmi() {
        return emi;
    }
}
