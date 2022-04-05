package com.example.givetake.model.singleton;

import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManagerSingleton {
    private static ProductManager productManager;

    public static synchronized ProductManager getProductManager() {
        if (productManager == null) productManager = new ProductManager();
        return productManager;
    }

    public static void setProductManager(Map<String, Map<String, Product>> productMap) {
        if (productMap==null) productManager = new ProductManager();
        else productManager = new ProductManager(productMap);
    }

    public static void setProductManager(ProductManager newProductManager) {
        if (newProductManager==null) productManager = new ProductManager();
        else productManager = newProductManager;
    }

    public static synchronized void createProductManager(UserManager userManager){
        ProductManager productManager = new ProductManager();
        for (User user :userManager.getUserMap().values()){
            for (Product product : user.getTradeProducts()){
                productManager.addProduct(product);
            }
        }
        setProductManager(productManager);
    }
}
