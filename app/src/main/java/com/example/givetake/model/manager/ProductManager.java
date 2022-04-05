package com.example.givetake.model.manager;

import com.example.givetake.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManager {
    Map<String, Map<String, Product>> productMap;

    public ProductManager(Map<String, Map<String, Product>> productMap) {
        this.productMap = productMap;
    }

    public ProductManager() {
        productMap = new HashMap<>();
    }


    public void addProduct(Product product){
        if (productMap.get(product.getTag())==null){
            productMap.put(product.getTag(), new HashMap<>());
            productMap.get(product.getTag()).put(product.getId(), product);
        }
        else {
            productMap.get(product.getTag()).put(product.getId(), product);
        }
    }



}
