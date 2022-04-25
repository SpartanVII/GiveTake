package com.example.givetake.presenter;

import android.net.Uri;

import com.example.givetake.model.Product;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.example.givetake.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

import java.util.ArrayList;
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
        //productManager.addProduct(productWithKey);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void modifyProduct(Product oldProduct, Product newProduct){
        userManager.deleteProduct(oldProduct);
        userManager.modifyProduct(newProduct);
        //productManager.deleteProduct(oldProduct);
        //productManager.addProduct(newProduct);
        dataBaseClass.save(userManager.getUser(newProduct.getOwner()));
    }

    public void deleteProduct(Product product){
        //productManager.deleteProduct(product);
        userManager.deleteProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void addFavoriteProduct(Product product){
        userManager.addFavoriteProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void deleteFavoriteProduct(Product product){
        userManager.deleteFavoriteProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public boolean isFavorite(Product product){
        return userManager.getUser(product.getOwner()).isFavorite(product.getId());
    }

    public List<String> getFavoriteProducts(String key){
        return getUser(key).getFavvoriteProducts();
    }

    public List<Product> getProductsByTag(String tag){
        return productManager.getProductsByTag(tag);
    }

    public Product getProduct(String key){
        return productManager.getProduct(key);
    }

    public List<Product> getProductListUsingKeys(List<String> keyList){
        List<Product> productList = new ArrayList<>();
        List<Product> deleteProductList = new ArrayList<>();
        for (String key : keyList){
            Product product = productManager.getProduct(key);

            if (product==null){
                product = new Product(key, key.split("#")[0]);
                deleteProductList.add(product);
            }
            else productList.add(product);
        }

        for (Product deleteProduct : deleteProductList){
            userManager.deleteFavoriteProduct(deleteProduct);
        }
        return productList;
    }


    public void addUser(User user){
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

    public String uploadImage(Uri filePath){
        return dataBaseClass.uploadImage(filePath);
    }

    public void initiality(){
        dataBaseClass.initialy();
        //ProductManagerSingleton.createProductManager(userManager);
    }
}
