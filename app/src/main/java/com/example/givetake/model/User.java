package com.example.givetake.model;

import java.time.LocalDate;
import java.util.List;

public class User {
    private String name;
    private String address;
    private int globalScore;
    private String mail;        //Private info
    private String genre;       //Private info
    private LocalDate birth;    //Private info
    List<Swap> swaps;
    List<Product> tradeProducts;
    List<Product> swapedproducts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getGlobalScore() {
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
