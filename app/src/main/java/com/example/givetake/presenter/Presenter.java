package com.example.givetake.presenter;

import com.example.givetake.model.Product;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.example.givetake.model.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

import java.util.Arrays;
import java.util.List;

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
        Product productWithKey = userManager.addProduct(product);
        productManager.addProduct(productWithKey);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void save(User user){
        userManager.putUser(user);
        dataBaseClass.save(user);
    }

    public void deleteUser(String userKey){
        userManager.deleteUser(userKey);
        dataBaseClass.deleteUser(userKey);
    }

    public User getUser(String mail){
        return userManager.getUser(mail);
    }

    public List<Product> getProductsByTag(String tag){
        return productManager.getProductsByTag(tag);
    }

    public Product getProduct(String key){
        return productManager.getProduct(key);
    }

    public void initiality(){
        dataBaseClass.initialy();
        ProductManagerSingleton.createProductManager(userManager);
    }
}
