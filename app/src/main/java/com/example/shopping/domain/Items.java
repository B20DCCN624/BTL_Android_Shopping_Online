package com.example.shopping.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class Items implements Serializable {
    private int id;
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double oldPrice;
    private double price;
    private int review;
    private float rating;
    private int numberinCart;

    public Items() {
    }


    public Items(String title, String description, ArrayList<String> picUrl, double oldPrice, double price, int review, float rating) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.oldPrice = oldPrice;
        this.price = price;
        this.review = review;
        this.rating = rating;
    }

    public Items(int id, String title, ArrayList<String> picUrl, double price, float rating) {
        this.id = id;
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.rating = rating;
    }

    public Items(String title, ArrayList<String> picUrl, double price, float rating) {
        this.title = title;
        this.picUrl = picUrl;
        this.price = price;
        this.rating = rating;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberinCart() {
        return numberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        this.numberinCart = numberinCart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
