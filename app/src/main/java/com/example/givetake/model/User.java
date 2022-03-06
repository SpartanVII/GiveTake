package com.example.givetake.model;

import android.location.Address;

import java.time.LocalDate;
import java.util.List;

public class User {
    private String name;
    private Address address;
    private double globalScore;
    private String mail;        //Private info
    private Gender gender;       //Private info
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
    List<Swap> swaps = new LinkedList<>();
    List<Product> tradeProducts = new LinkedList<>();
    List<Product> swapedProducts = new LinkedList<>();

    public User(String name, String address, String mail, String gender, LocalDate birth) {
        this.name = name;
        this.address = address;
        this.globalScore = 0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGender() {
        return gender.toString();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public List<Product> getSwapedProducts() {
        return swapedProducts;
    }

    public void setSwapedProducts(List<Product> swapedProducts) {
        this.swapedProducts = swapedProducts;
    }

    public Gender fromStrToGender(String gender){
        switch(gender){
            case("Hombre"):
                return Gender.MALE;
            case("Mujer"):
                return Gender.FEMALE;
            default:
                return Gender.OTHER;
        }
    }
}
