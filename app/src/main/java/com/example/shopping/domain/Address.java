package com.example.shopping.domain;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private String add;

    public Address() {
    }

    public Address(String add) {
        this.add = add;
    }

    public Address(int id, String add) {
        this.id = id;
        this.add = add;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }
    @Override
    public String toString() {
        return add;  // This ensures that the address string is used by the ArrayAdapter for display
    }
}
