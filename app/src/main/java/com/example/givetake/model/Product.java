package com.example.givetake.model;

import java.util.List;

public class Product {
    private String title;
    private String description;
    private String img;
    private String owner;
    private List<Tags> tags;

    public Product(String title, String description, String img, String owner, List<Tags> tags) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.owner = owner;
        this.tags = tags;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }
}
