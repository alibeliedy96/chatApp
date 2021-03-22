package com.example.chatapp.FirebaseUtils.Model;

public class User {
    String Id;
    String username;
    String email;
    String password;

    public User() {
    }

    public User( String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
