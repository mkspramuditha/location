package com.example.shan.location;

/**
 * Created by chamod on 1/15/17.
 */

public class User {
    private String user_id;
    private String password;
    private String emi;

    public User(String user_id,String emi, String password) {
        this.user_id = user_id;
        this.password = password;
        this.emi=emi;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmi() {
        return emi;
    }
}
