package com.example.givetake.model;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private String reviwedName;
    private String authorName;
    private String comentary;
    private String otherProductName;
    private Date reviewDate;
    private int score;
    private int extraPrice;
    private boolean isCompleted;

    public Review() {
    }

    public Review(String reviwedName, String authorName, String comentary, String otherProductName, Date reviewDate, int score, int extraPrice, boolean isCompleted) {
        this.reviwedName = reviwedName;
        this.authorName = authorName;
        this.comentary = comentary;
        this.otherProductName = otherProductName;
        this.reviewDate = reviewDate;
        this.score = score;
        this.extraPrice = extraPrice;
        this.isCompleted = isCompleted;
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

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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
}
