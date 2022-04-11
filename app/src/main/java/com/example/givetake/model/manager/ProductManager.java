package com.example.givetake.model.manager;

import com.example.givetake.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
            System.out.println("lala");
            System.out.println(productMap.get(product.getTag()));
            System.out.println(product);
            productMap.get(product.getTag()).put(product.getId(), product);
        }
    }

    public void deleteProduct(Product product){
        Objects.requireNonNull(productMap.get(product.getTag())).remove(product.getId());
    }

    public List<Product> getProductsByTag(@Nullable String tag){
        List<Product> productList = new ArrayList<>();
        if (tag==null) tag = "Todos";
        if (tag.equals("Todos")){
            for (Map<String, Product> map : productMap.values()){
                productList.addAll(map.values());
            }
        }
        else{
            if (productMap.get(tag)==null) return productList;
            productList.addAll(productMap.get(tag).values());
        }
        return productList;
    }

    /*public List<Product> getProductsByTagAndExludeMyPorducts(@Nullable String tag, String userMail){
        return productManager.getProductsByTag(tag);
    }
    
     */

    public  Product getProduct(String key){
        for (Map<String, Product> categoryMap: productMap.values()){
            if (categoryMap.get(key)!=null) return categoryMap.get(key);
        }
        return null;
    }
}
