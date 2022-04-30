package com.example.givetake.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Review implements Serializable, Comparable {
    private String authorName;
    private String reviwedName;
    private String comentary;
    private String productName;
    private String otherProductName;
    private String productImg;
    private Date reviewDate;
    private int daysShowedToComplete;
    private int score;
    private int extraPrice;
    private boolean isCompleted;

    public Review() {
        this.daysShowedToComplete = 8;
        this.extraPrice = 0;
    }

    public Review(String authorName, String reviwedName, String comentary, String productName, String otherProductName, String productImg, Date reviewDate, int score, int extraPrice, boolean isCompleted) {
        this.authorName = authorName;
        this.reviwedName = reviwedName;
        this.comentary = comentary;
        this.productName = productName;
        this.otherProductName = otherProductName;
        this.productImg = productImg;
        this.reviewDate = reviewDate;
        this.daysShowedToComplete = 8;
        this.score = score;
        this.extraPrice = extraPrice;
        this.isCompleted = isCompleted;
    }

    public Review(Review review) {
        this.authorName = review.getAuthorName();
        this.reviwedName = review.getReviwedName();
        this.comentary = review.getComentary();
        this.productName = review.getProductName();
        this.otherProductName = review.getOtherProductName();
        this.productImg = review.getProductImg();
        this.reviewDate = review.getReviewDate();
        this.daysShowedToComplete = 8;
        this.score = review.getScore();
        this.extraPrice = review.getExtraPrice();
        this.isCompleted = review.isCompleted();
    }

    public String getReviwedName() {
        return reviwedName;
    }

    public void setReviwedName(String reviwedName) {
        this.reviwedName = reviwedName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getComentary() {
        return comentary;
    }

    public void setComentary(String comentary) {
        this.comentary = comentary;
    }

    public String getOtherProductName() {
        return otherProductName;
    }

    public void setOtherProductName(String otherProductName) {
        this.otherProductName = otherProductName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getDaysShowedToComplete() {
        return daysShowedToComplete;
    }

    public void setDaysShowedToComplete(int daysShowedToComplete) {
        this.daysShowedToComplete = daysShowedToComplete;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice = extraPrice;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public int compareTo(Object obj) {
        Review review = (Review) obj;
        if (review.isCompleted && !isCompleted) return -1;
        else if (!review.isCompleted && isCompleted) return 1;
        else return 0;
    }

    public void decrementDaysToComplete(){
        daysShowedToComplete-=1;
    }

    public void reverseAuthorAndReviewed(){
        String author = authorName;
        authorName = reviwedName;
        reviwedName = author;
    }

    public void reverseProducts(){
        String product= productName;
        productName = otherProductName;
        otherProductName = product;
        isCompleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return reviwedName.equals(review.reviwedName) && authorName.equals(review.authorName)  && productName.equals(review.productName) && otherProductName.equals(review.otherProductName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviwedName, authorName, comentary, productName);
    }

    public double getScoreFromExperience(String reputation){
        if (reputation.equals("Horrible")) return 0;
        if (reputation.equals("Mala")) return 2.5;
        if (reputation.equals("Regular")) return 5;
        if (reputation.equals("Buena")) return 7.5;
        if (reputation.equals("Excelente")) return 10;
        return 5;
    }

    public String getExperienceFromScore(double score){
        if (score == 0) return "Horrible";
        if (score == 2.5) return "Mala";
        if (score == 5) return "Regular";
        if (score == 7.5) return "Buena";
        if (score == 10) return "Excelente";
        return "";
    }

    public String getExperienceFromScore(){
        if (score == 0) return "horrible";
        if (score == 2.5) return "mala";
        if (score == 5) return "regular";
        if (score == 7.5) return "buena";
        if (score == 10) return "excelente";
        return "";
    }

    public String getShortName() {
        String[] nameSplit = authorName.split(" ");
        if (nameSplit.length == 1) return nameSplit[0];
        return nameSplit[0] + " " + nameSplit[1].charAt(0)+".";
    }

    public String getFormatReviewDate() {

        return new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es")).format(reviewDate);
    }

}
