package com.example.shan.location;

/**
 * Created by chamod on 1/15/17.
 */

public class User {
    private String user_id;
    private String password;

    public User(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }
}
