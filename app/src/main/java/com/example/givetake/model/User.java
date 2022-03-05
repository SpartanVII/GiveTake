package com.example.givetake.model;

import android.location.Address;

import java.time.LocalDate;
import java.util.List;

public class User {
    private String name;
    private Address address;
    private double globalScore;
    private String mail;        //Private info
    private String genre;       //Private info
    private LocalDate birth;    //Private info
    private List<Swap> swaps;
    private List<Product> tradeProducts;
    private List<Product> swapedproducts;

    public void setGlobalScore(double globalScore) {
        this.globalScore = globalScore;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getGlobalScore() {
        return globalScore;
    }

    public void setGlobalScore(int globalScore) {
        this.globalScore = globalScore;
    }

    public List<Swap> getSwaps() {
        return swaps;
    }

    public void setSwaps(List<Swap> swaps) {
        this.swaps = swaps;
    }

    public List<Product> getTradeProducts() {
        return tradeProducts;
    }

    public void setTradeProducts(List<Product> tradeProducts) {
        this.tradeProducts = tradeProducts;
    }

    public List<Product> getSwapedproducts() {
        return swapedproducts;
    }

    public void setSwapedproducts(List<Product> swapedproducts) {
        this.swapedproducts = swapedproducts;
    }
}
