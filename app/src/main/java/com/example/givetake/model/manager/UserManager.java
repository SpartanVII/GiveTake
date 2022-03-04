package com.example.givetake.model.manager;

import com.example.givetake.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    Map<String, User> userMap = new HashMap<>();

    public UserManager(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public User getUser(String nickName){
        return userMap.get(nickName);
    }
}
