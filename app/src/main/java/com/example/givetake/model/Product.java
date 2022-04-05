package com.example.givetake.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private String title;
    private String description;
    private String img;
    private String owner;
    private String tag;

    public Product(String title, String description, String img, String owner, String tag) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.owner = owner;
        this.tag = tag;
    }

    public Product() {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
