package com.example.givetake.model.singleton;

import com.example.givetake.model.User;
import com.example.givetake.model.manager.UserManager;

import java.util.Map;

public class UserManagerSingleton {
    private static UserManager userManager;

    public static synchronized UserManager getUserManager() {
        if (userManager == null) userManager = new UserManager();
        return userManager;
    }

    public static void setUserManager(Map<String, User> userMap) {
        if (userMap == null) System.out.println("vacio");
        else System.out.println("No vacia");
        if (userMap == null) userManager = new UserManager();
        else userManager = new UserManager(userMap);

    }
}
