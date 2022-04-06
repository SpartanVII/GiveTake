package com.example.givetake.model.manager;

import com.example.givetake.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

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

    public List<Product> getProductsByTag(@Nullable String tag){
        List<Product> productList = new ArrayList<>();
        if (tag==null) tag="Todos";
        if (tag.equals("Todos")){
            for (Map<String, Product> map : productMap.values()){
                productList.addAll(map.values());
            }
        }
        else{
            productList.addAll(productMap.get(tag).values());
        }
        return productList;
    }



}
