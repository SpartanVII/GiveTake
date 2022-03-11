package com.example.givetake.presenter;

import com.example.givetake.model.Product;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
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
        userManager = UserManagerSingleton.getUserManager();
        productManager = ProductManagerSingleton.getProductManager();
    }

    public void addProduct(Product product){
        userManager.addProduct(product);
        productManager.addProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void addUser(User user){
        userManager.addUser(user);
        dataBaseClass.saveNewUser(user);
    }

    public void modifyUser(User user){
        userManager.modifyUser(user);
        dataBaseClass.saveNewUser(user);
    }


    public User getUser(String mail){
        return userManager.getUser(mail);
    }

    public void initiality(){
        dataBaseClass.initialy();
    }
}
