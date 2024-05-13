package com.example.shopping.domain;

public class Profile {
    private int id;
    private String username;
    private String email;
    private String date;
    private String contact;

    public Profile(String username, String email, String date, String contact) {
        this.username = username;
        this.email = email;
        this.date = date;
        this.contact = contact;
    }

    public Profile(int id, String username, String email, String date, String contact) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.date = date;
        this.contact = contact;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
