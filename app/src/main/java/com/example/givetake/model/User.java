package com.example.givetake.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    private String name;
    private MyAddress address;
    private double globalScore;
    private String mail;        //Private info
    private Gender gender;      //Private info
    private Date birth;         //Private info
    private int nProduct;       //Private info
    private List<Review> reviewsForMe;
    private List<Review> reviewsWrittenByMe;
    private List<Product> tradeProducts;
    private List<Product> swapedProducts;
    private List<String> favoriteProductsKeys;

    public User(String name, MyAddress address, String mail, String gender, Date birth) {
        this.name = name;
        this.address = address;
        this.globalScore = 5.0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
        reviewsForMe = new ArrayList<>();
        reviewsWrittenByMe = new ArrayList<>();
        tradeProducts = new ArrayList<>();
        swapedProducts = new ArrayList<>();
        favoriteProductsKeys = new ArrayList<>();
        this.nProduct = 0;
    }

    public User(String name, MyAddress address, String mail, String gender, Date birth, int nProduct) {
        this.name = name;
        this.address = address;
        this.globalScore = 5.0;
        this.mail = mail;
        this.gender = fromStrToGender(gender);
        this.birth = birth;
        reviewsForMe = new ArrayList<>();
        reviewsWrittenByMe = new ArrayList<>();
        tradeProducts = new ArrayList<>();
        swapedProducts = new ArrayList<>();
        favoriteProductsKeys = new ArrayList<>();
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
        return fromGenderToString(gender);
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        String[] nameSplit = name.split(" ");
        if (nameSplit.length == 1) return nameSplit[0];
        return nameSplit[0] + " " + nameSplit[1].charAt(0);
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

    public List<Review> getReviewsForMe() {
        return reviewsForMe;
    }

    public void setReviewsForMe(List<Review> reviewsForMe) {
        this.reviewsForMe = reviewsForMe;
    }

    public List<Review> getReviewsWrittenByMe() {
        return reviewsWrittenByMe;
    }

    public void setReviewsWrittenByMe(List<Review> reviewsWrittenByMe) {
        this.reviewsWrittenByMe = reviewsWrittenByMe;
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

    public List<String> getFavoriteProducts() {
        return favoriteProductsKeys;
    }

    public void setFavoriteProductsKeys(List<String> favoriteProductsKeys) {
        this.favoriteProductsKeys = favoriteProductsKeys;
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

    public String getGlobalReputation(){
        String reputation = "";
        //If the valoration stays bellow 5 with more than 3 swapped products we ban the user
        if (globalScore>2.4) reputation = "mala";
        if (globalScore>4.9) reputation = "estandar";
        if (globalScore>7.4) reputation = "buena";
        if (globalScore>8.9) reputation = "excenlente";
        return reputation;
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
        for (int i = 0; i< favoriteProductsKeys.size(); i++){
            if (favoriteProductsKeys.get(i).equals(id)){
                return true;
            }
        }
        return false;
    }

    public void addFavoriteProduct(String id){
        favoriteProductsKeys.add(id);
    }

    public void deleteFavoriteProduct(String id){
        favoriteProductsKeys.remove(id);
    }

    public int obtainAndIncrementNproduct(){
        nProduct+=1;
        return nProduct-1;
    }

    public List<Review> getUncompletedReviews(){
        List<Review> copyReviews = new ArrayList<>(reviewsWrittenByMe);
        copyReviews.sort(Review::compareTo);
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < copyReviews.size(); i++) {
            Review review = copyReviews.get(i);
            if (review.isCompleted()) break;
            reviewList.add(review);
        }
        return reviewList;
    }

    public void decrementDaysToCompleteReview(Review review){
        int i = reviewsWrittenByMe.indexOf(review);
        Review decrementReview = reviewsWrittenByMe.get(i);
        decrementReview.decrementDaysToComplete();
        if (decrementReview.getDaysShowedToComplete()==0) reviewsWrittenByMe.remove(review);
    }

    public void addReviewWrittenByMe(Review review){
        reviewsWrittenByMe.remove(review);
        reviewsWrittenByMe.add(review);
    }

    public void addReviewForMe(Review review){
        if (reviewsForMe.size() == 0)  globalScore = review.getScore();
        else globalScore = (globalScore * reviewsForMe.size() + review.getScore())/reviewsForMe.size()+1;
        reviewsForMe.add(review);
    }

    public void swapProduct(Product product){
        tradeProducts.remove(product);
        swapedProducts.add(product);
    }


}
