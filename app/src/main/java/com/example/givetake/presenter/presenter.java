package com.example.givetake.presenter;

import com.example.givetake.model.Product;
import com.example.givetake.model.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

public class presenter {
    DataBaseClass dataBaseClass;
    UserManager userManager;
    ProductManager productManager;

    public presenter(DataBaseClass dataBaseClass, UserManager userManager) {
        this.dataBaseClass = dataBaseClass;
        this.userManager = userManager;
    }

    public presenter() {
        dataBaseClass = new DataBaseClass();
        userManager = new UserManager();
    }

    public void addProduct(Product product){
        userManager.addProduct(product);
    }


}
