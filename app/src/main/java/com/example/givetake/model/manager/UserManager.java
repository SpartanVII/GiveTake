package com.example.givetake.model.manager;

import com.example.givetake.model.Product;
import com.example.givetake.model.Review;
import com.example.givetake.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private Map<String, User> userMap;

    public UserManager(Map<String, User> userMap) {
        this.userMap = userMap;
    }
    public UserManager(){
        userMap = new HashMap<>();
    }

    public User getUser(String mail){
        return userMap.get(mail.split("@")[0]);
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    public Product addProduct(Product product){
        User user =  getUser(product.getOwner());
        product.setId(product.getOwner().split("@")[0]+"#"+user.obtainAndIncrementNproduct());
        return user.addTradableProduct(product);
    }

    public Product modifyProduct(Product product){
        User user =  getUser(product.getOwner());
        return user.addTradableProduct(product);
    }

    public void deleteProduct(Product product){
        getUser(product.getOwner()).deleteTradableProduct(product);
    }

    public void addFavoriteProduct(Product product, String userKey){
        getUser(userKey).addFavoriteProduct(product.getId());
    }

    public void deleteFavoriteProduct(Product product, String userKey){
        User user =  getUser(userKey);
        user.deleteFavoriteProduct(product.getId());
    }

    public void deleteFavoriteProduct(String productKey, String userKey){
        User user =  getUser(userKey);
        user.deleteFavoriteProduct(productKey);
    }

    public List<String> getFavoriteProductsKey(String userKey){
        return getUser(userKey).getFavoriteProducts();
    }

    public List<Review> getUncompletedReviews(String userkey){
        return getUser(userkey).getUncompletedReviews();
    }

    public List<Review> getReviewsForMe(String userkey){
        return getUser(userkey).getReviewsForMe();
    }

    public void decrementDaysToCompleteReview(Review review, String userKey){
        getUser(userKey).decrementDaysToCompleteReview(review);
    }

    public void addReviewToMe(Review review, String userKey){
        getUser(userKey).addReviewWrittenByMe(review);
    }

    public void addReviewToTheOther(Review review, String userKey){
        getUser(userKey).addReviewForMe(review);
        Review inverseReview = new Review(review);
        inverseReview.reverseAuthorAndReviewed();
        inverseReview.reverseProducts();
        getUser(userKey).addReviewWrittenByMe(review);
    }

    public void swapProduct(Product product){
        getUser(product.getOwner()).swapProduct(product);
    }

    public void putUser(User user){
         userMap.put(user.getMail().split("@")[0], user);
    }

    public void deleteUser(String userKey){
        userMap.remove(userKey);
    }

    public void modifyUser(User user){
        userMap.replace(user.getMail().split("@")[0], user);
    }


}
