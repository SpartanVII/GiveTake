package com.example.givetake.model.manager;

import com.example.givetake.model.Product;
import com.example.givetake.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private Map<String, User> userMap;

    public UserManager(Map<String, User> userMap) {
        this.userMap = userMap;
    }
    public UserManager(){
        userMap = new HashMap<>();
    }

    public User getUser(String mail){
        return userMap.get(mail.split("@")[0]);
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public Product addProduct(Product product){
        User user =  getUser(product.getOwner());
        product.setId(product.getOwner().split("@")[0]+"#"+user.obtainAndIncrementNproduct());
        return user.addTradableProduct(product);
    }

    public Product modifyProduct(Product product){
        User user =  getUser(product.getOwner());
        return user.addTradableProduct(product);
    }

    public void deleteProduct(Product product){
        getUser(product.getOwner()).deleteTradableProduct(product);
    }

    public void addFavoriteProduct(Product product){
        getUser(product.getOwner()).addFavoriteProduct(product.getId());
    }

    public void deleteFavoriteProduct(Product product){
        User user =  getUser(product.getOwner());
        user.deleteFavoriteProduct(product.getId());
    }

    public List<String> getFAvoriteProducts(String key){
        return getUser(key).getFavvoriteProducts();
    }

    public void putUser(User user){
         userMap.put(user.getMail().split("@")[0], user);
    }

    public void deleteUser(String userKey){
        userMap.remove(userKey);
    }

    public void modifyUser(User user){
        userMap.replace(user.getMail().split("@")[0], user);
    }


}
