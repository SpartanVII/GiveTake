package com.example.givetake.model.manager;

import com.example.givetake.model.Product;
import com.example.givetake.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    Map<String, User> userMap;

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

    public void addProduct(Product product){
        getUser(product.getOwner()).getTradeProducts().add(product);
    }

    public void addUser(User user){
        userMap.put(user.getMail().split("@")[0], user);
    }


}
