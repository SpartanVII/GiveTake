package com.example.givetake.model.singleton;

import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.model.manager.ProductManager;

import java.util.List;
import java.util.Map;

public class ProductManagerSingleton {
    private static ProductManager productManager;

    public static synchronized ProductManager getProductManager() {
        if (productManager == null) productManager = new ProductManager();
        return productManager;
    }

    public static void setProductManager(Map<String, List<Product>> productMap) {
        if (productMap==null) productManager = new ProductManager();
        else productManager = new ProductManager(productMap);
    }
}
