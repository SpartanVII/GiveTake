package com.example.givetake.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    private String name;
    private MyAddress address;
    private double globalScore;
    private String mail;        //Private info
    private Gender gender;      //Private info
    private Date birth;    //Private info
    private List<Review> reviews;
    private List<Product> tradeProducts;
    private List<Product> swapedProducts;
    private List<String> favProducts;
    private int nProduct;       //Private info

    public User(String name, MyAddress address, String mail, String gender, Date birth) {
        this.name = name;
        this.address = address;
        this.globalScore = 5.0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
        reviews = new ArrayList<>();
        tradeProducts = new ArrayList<>();
        swapedProducts = new ArrayList<>();
        favProducts = new ArrayList<>();
        this.nProduct = 0;
    }

    public User(String name, MyAddress address, String mail, String gender, Date birth, int nProduct) {
        this.name = name;
        this.address = address;
        this.globalScore = 5.0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
        reviews = new ArrayList<>();
        tradeProducts = new ArrayList<>();
        swapedProducts = new ArrayList<>();
        favProducts = new ArrayList<>();
        this.nProduct = nProduct;
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

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
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

    public MyAddress getAddress() {
        return address;
    }

    public void setAddress(MyAddress address) {
        this.address = address;
    }

    public double getGlobalScore() {
        return globalScore;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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

    public List<String> getFavProducts() {
        return favProducts;
    }

    public void setFavProducts(List<String> favProducts) {
        this.favProducts = favProducts;
    }

    public int getnProduct() {
        return nProduct;
    }

    public void setnProduct(int nProduct) {
        this.nProduct = nProduct;
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public Gender fromStrToGender(String gender){
        if (gender.equals("Hombre") || gender.equals("MALE")) return Gender.MALE;
        if (gender.equals("Mujer") || gender.equals("FEMALE")) return Gender.FEMALE;
        else return Gender.OTHER;
    }

    public String fromGenderToString(Gender gender){
        if (gender.name().equals("MALE")) return "Hombre";
        else if (gender.name().equals("FEMALE")) return "Mujer";
        else return "Otro";
    }

    public String obtainGenderToString(){
        if (gender.name().equals("MALE")) return "Hombre";
        else if (gender.name().equals("FEMALE")) return "Mujer";
        else return "Otro";
    }

    public String obtainAddressLine(){
        return address.getAddressLine();
    }

    public Product addTradableProduct(Product product){
        tradeProducts.add(product);
        return product;
    }
    public void deleteTradableProduct(Product product){
        for (int i = 0 ; i<tradeProducts.size(); i++){
            if (tradeProducts.get(i).equals(product)){
                tradeProducts.remove(i);
                break;
            }
        }
    }

    public boolean isFavorite(String id){
        for (int i = 0 ; i<favProducts.size(); i++){
            if (favProducts.get(i).equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addFavoriteProduct(String id){
        favProducts.add(id);
    }

    public void deleteFavoriteProduct(String id){
        favProducts.remove(id);
    }

    public int obtainAndIncrementNproduct(){
        nProduct+=1;
        return nProduct-1;
    }
}
