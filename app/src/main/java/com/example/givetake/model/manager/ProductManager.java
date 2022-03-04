package com.example.givetake.model.manager;

import com.example.givetake.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManager {
    Map<String, List<Product>> productMap = new HashMap<>();

    public ProductManager(Map<String, List<Product>> productMap) {
        this.productMap = productMap;
    }

    public List<Product> getProducts(String nickName){
        return productMap.get(nickName);
    }

}
