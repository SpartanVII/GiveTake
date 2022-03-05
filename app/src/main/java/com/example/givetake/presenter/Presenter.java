package com.example.givetake.presenter;

import com.example.givetake.model.Product;
import com.example.givetake.model.Singleton;
import com.example.givetake.model.User;
import com.example.givetake.model.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

public class Presenter {
    DataBaseClass dataBaseClass;
    UserManager userManager;
    ProductManager productManager;


    public Presenter() {
        dataBaseClass = new DataBaseClass();
        userManager = Singleton.getUserManager();
    }

    public void addProduct(Product product){
        userManager.addProduct(product);
    }

    public User getUser(String mail){
        return userManager.getUser(mail);
    }

    public void initiality(){
        dataBaseClass.initialy();
    }
}
