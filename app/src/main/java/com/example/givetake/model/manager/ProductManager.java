package com.example.givetake.model.manager;

import com.example.givetake.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManager {
    Map<String, List<Product>> productMap;

    public ProductManager(Map<String, List<Product>> productMap) {
        this.productMap = productMap;
    }

    public ProductManager() { productMap = new HashMap<>();
    }

    public List<Product> getProducts(String mail){
        return productMap.get(mail);
    }

    public void addProduct(Product product){
        List<Product> products = productMap.get(product.getTag());
        if(products == null) products = new ArrayList<>();
        products.add(product);
    }

}
