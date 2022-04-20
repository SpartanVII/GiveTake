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

    public Product(String id, String owner) {
        this.id = id;
        this.owner = owner;
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

    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Id: ").append(id).append(", ");
        strBuilder.append("Nombre: ").append(title).append(", ");
        strBuilder.append("Descripción: ").append(description).append(", ");
        strBuilder.append("Propietario: ").append(owner).append(", ");
        strBuilder.append("Categoría: ").append(tag);
        return strBuilder.toString();
    }

    public boolean equals(Object obj){
        Product otherProduct = (Product) obj;
        if (this.id.equals(otherProduct.getId()))
            return true;
        return false;
    }
}
