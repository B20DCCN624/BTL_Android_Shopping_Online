package com.example.shopping.domain;

public class Payment {
    private int id;
    private double priceTotal;
    private String date;

    public Payment() {
    }

    public Payment(double priceTotal, String date) {
        this.priceTotal = priceTotal;
        this.date = date;
    }

    public Payment(int id, double priceTotal, String date) {
        this.id = id;
        this.priceTotal = priceTotal;
        this.date = date;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
