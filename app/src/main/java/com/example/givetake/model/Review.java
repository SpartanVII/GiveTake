package com.example.givetake.model;

public class Review {
    private String reviwerName;
    private int score;
    private String comentary;
    private String otherProductName;
    private int extraPrice;

    public Review(String reviwerName, int score, String comentary, String otherProductName, int extraPrice) {
        this.reviwerName = reviwerName;
        this.score = score;
        this.comentary = comentary;
        this.otherProductName = otherProductName;
        this.extraPrice = extraPrice;
    }

    public String getReviwerName() {
        return reviwerName;
    }

    public void setReviwerName(String reviwerName) {
        this.reviwerName = reviwerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public int getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice = extraPrice;
    }
}
