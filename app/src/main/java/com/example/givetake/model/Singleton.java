package com.example.givetake.model;

import com.example.givetake.model.manager.UserManager;

import java.util.Map;

public class Singleton {
    private static UserManager userManager;

    public static synchronized UserManager getUserManager() {
        if (userManager == null) userManager = new UserManager();
        return userManager;
    }

    public static void setUserManager(Map<String,User> userMap) {
        userManager = new UserManager(userMap);
    }
}
