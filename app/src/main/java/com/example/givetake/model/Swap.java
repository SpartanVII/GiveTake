package com.example.givetake.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Swap implements Serializable {
    private String userName;
    private LocalDate date;
    private Product product;
    private Review review;

    public Swap(String userName, LocalDate date, Product product, Review review) {
        this.userName = userName;
        this.date = date;
        this.product = product;
        this.review = review;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
