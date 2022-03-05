package com.example.givetake.presenter;

import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.model.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

public class Presenter {
    DataBaseClass dataBaseClass;
    UserManager userManager;
    ProductManager productManager;

    public Presenter(DataBaseClass dataBaseClass, UserManager userManager) {
        this.dataBaseClass = dataBaseClass;
        this.userManager = userManager;
    }

    public Presenter() {
        dataBaseClass = new DataBaseClass();
        userManager = new UserManager();
        dataBaseClass.initialy(userManager);
    }

    public void addProduct(Product product){
        userManager.addProduct(product);
    }

    public User getUser(String mail){
        return userManager.getUser(mail);
    }
}
