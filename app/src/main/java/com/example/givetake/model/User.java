package com.example.givetake.model;

import android.location.Address;

import com.google.android.material.timepicker.TimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class User implements Serializable {
    private String name;
    private Address address;
    private double globalScore;
    private String mail;        //Private info
    private Gender gender;       //Private info
    private LocalDate birth;    //Private info
    private List<Swap> swaps;
    private List<Product> tradeProducts;
    private List<Product> swapedProducts;

    public User(String name, Address address, String mail, String gender, LocalDate birth) {
        this.name = name;
        this.address = address;
        this.globalScore = 0.0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
    }

    public void setGlobalScore(double globalScore) {
        this.globalScore = globalScore;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Gender getGenre() {
        return gender;
    }

    public void setGenre(String genre) {
        this.gender = gender;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender.toString();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
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

    public String fromGenderToString(Gender gender){
        if (gender.name().equals("MALE")) return "Hombre";
        else if (gender.name().equals("FEMALE")) return "Mujer";
        else return "Otro";
    }

    public String getGenderToString(){
        if (gender.name().equals("MALE")) return "Hombre";
        else if (gender.name().equals("FEMALE")) return "Mujer";
        else return "Otro";
    }

    public String getAddressToString(){
        return address.getLocality()+address.getSubAdminArea()+address.getAdminArea();
    }
}
