package com.example.givetake.presenter;

import android.net.Uri;

import com.example.givetake.model.Product;
import com.example.givetake.model.Review;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.example.givetake.dataBase.DataBaseClass;
import com.example.givetake.model.manager.ProductManager;
import com.example.givetake.model.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Presenter {
    DataBaseClass dataBaseClass;
    UserManager userManager;
    ProductManager productManager;


    public Presenter() {
        dataBaseClass = new DataBaseClass();
        userManager = UserManagerSingleton.getUserManager();
        productManager = ProductManagerSingleton.getProductManager();
    }

    public void addProduct(Product product){
        Product productWithKey = userManager.addProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void modifyProduct(Product oldProduct, Product newProduct){
        userManager.deleteProduct(oldProduct);
        userManager.modifyProduct(newProduct);
        dataBaseClass.save(userManager.getUser(newProduct.getOwner()));
    }

    public void deleteProduct(Product product){
        userManager.deleteProduct(product);
        dataBaseClass.save(userManager.getUser(product.getOwner()));
    }

    public void addFavoriteProduct(Product product, String userKey){
        userManager.addFavoriteProduct(product, userKey);
        dataBaseClass.save(userManager.getUser(userKey));
    }

    public void deleteFavoriteProduct(Product product, String userKey){
        userManager.deleteFavoriteProduct(product, userKey);
        dataBaseClass.save(userManager.getUser(userKey));
    }

    public boolean isFavorite(Product product, String userKey){
        return userManager.getUser(userKey).isFavorite(product.getId());
    }

    public List<String> getFavoriteProductsKey(String userKey){
        return userManager.getFavoriteProductsKey(userKey);
    }

    public List<Product> getProductsByTag(String tag){
        return productManager.getProductsByTag(tag);
    }

    public Product getProduct(String key){
        return productManager.getProduct(key);
    }

    public List<Product> getFavoriteProducts(String userKey){
        List<String> favList = userManager.getFavoriteProductsKey(userKey);
        List<Product> productList = new ArrayList<>();
        List<String> deleteProductList = new ArrayList<>();
        for (String favKey : favList){
            Product product = productManager.getProduct(favKey);

            if (product==null) deleteProductList.add(favKey);
            else productList.add(product);
        }

        for (String deleteProduct : deleteProductList){
            userManager.deleteFavoriteProduct(deleteProduct, userKey);
        }
        return productList;
    }

    public List<Review> getUncompleteReviews(String userKey){
        return new ArrayList<>(userManager.getUncompletedReviews(userKey));
    }

    public List<Review> getReviewsForMe(String userKey){
        return new ArrayList<>(userManager.getReviewsForMe(userKey));
    }

    public void decrementDaysToCompleteReview(Review review, String userKey){
        userManager.decrementDaysToCompleteReview(review, userKey);
        dataBaseClass.save(getUser(userKey));
    }

    public void addReviewWrittenByMe(Review review, String userKey){
        userManager.addReviewWrittenByMe(review, userKey);
        dataBaseClass.save(getUser(userKey));
    }

    public void addReviewForTheOtherToComplete(Review review, String userKey){
        userManager.addReviewForTheOtherToComplete(review, userKey);
        dataBaseClass.save(getUser(userKey));
    }

    public void addReviewForTheOther(Review review, String userKey){
        userManager.addReviewForTheOther(review, userKey);
        dataBaseClass.save(getUser(userKey));
    }

    public void swapProduct(Product product){
        userManager.swapProduct(product);
        dataBaseClass.save(getUser(product.getOwner()));
    }
    public void addUser(User user){
        userManager.putUser(user);
        dataBaseClass.save(user);
    }

    public void deleteUser(String userKey){
        userManager.deleteUser(userKey);
        dataBaseClass.deleteUser(userKey);
    }

    public User getUser(String mail){
        return userManager.getUser(mail);
    }

    public String uploadImage(Uri filePath){
        return dataBaseClass.uploadImage(filePath);
    }

    public void deleteImage(String imageUrlWithoutProcessing){
        dataBaseClass.deleteImage(imageUrlWithoutProcessing);
    }

    public void sendReport(List<String> reasons, String reportedUserKey, String reporterUSerKey){
        dataBaseClass.sendComplaintUSer(reasons, reportedUserKey, reporterUSerKey);
    }

    public void initialize(){
        dataBaseClass.initialize();
    }
}
