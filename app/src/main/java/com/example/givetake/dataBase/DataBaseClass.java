package com.example.givetake.dataBase;

import android.net.Uri;
import android.util.Log;

import com.example.givetake.model.json.LocalDateAdapter;
import com.example.givetake.model.manager.UserManager;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataBaseClass {
    private final FirebaseDatabase db;
    private final FirebaseStorage storage;
    private final StorageReference storageReference;
    private final Gson gson;

    private final static String USERS_COLLECTION = "Users";
    private final static String REPORT_COLLECTION = "Reports";


    public DataBaseClass() {
        this.db = FirebaseDatabase.getInstance("https://givetake-9f7af-default-rtdb.europe-west1.firebasedatabase.app/");
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        db.goOnline();
    }

    public void save(User user){
            DatabaseReference userReference = db.getReference(USERS_COLLECTION).child(user.getMail().split("@")[0]);
            userReference.setValue(gson.toJson(user));
            /*
            userReference.child("name").setValue(user.getName());
            userReference.child("address").setValue(user.getAddress());
            userReference.child("gloablScore").setValue(user.getGlobalScore());
            userReference.child("mail").setValue(user.getMail());
            userReference.child("gender").setValue(user.getGender());
            userReference.child("birth").setValue(user.getBirth());
            userReference.child("swaps").setValue(user.getSwaps());
            userReference.child("tradeProducts").setValue(user.getTradeProducts());
            userReference.child("swapedProducts").setValue(user.getSwapedProducts());
            userReference.child("favProducts").setValue(user.getFavProducts());
            userReference.child("nProduct").setValue(user.getnProduct());
            */
             //userReference.setValue(user);
    }



    public void deleteUser(String userKey){
        db.getReference(USERS_COLLECTION).child(userKey).removeValue();
    }

    public void initialize(){
        Type userType = new TypeToken<Map<String, User>>(){}.getType();
        DatabaseReference userReference = db.getReference(USERS_COLLECTION);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                Map<String, User> userMap =(HashMap<String, User>) dataSnapshot.getValue();
                UserManagerSingleton.setUserManager(userMap);
                ProductManagerSingleton.createProductManagerWithMap(userMap);
                */
                String json = String.valueOf(dataSnapshot.getValue());
                UserManagerSingleton.setUserManager(gson.fromJson(json, userType));
                ProductManagerSingleton.createProductManagerWithMap(gson.fromJson(json, userType));
                Log.w("Firebase", "Data loaded");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to load the data", error.toException());
            }
        });
    }

    public String uploadImage(Uri filePath) {
        String url = "images/"+ UUID.randomUUID().toString();
        StorageReference ref = storageReference.child(url);
        ref.putFile(filePath);

        return storageReference + url;
    }

    public void sendComplaintUSer(List<String> reasons,  String reportedUserKey, String reporterUserKey){
        DatabaseReference userReference = db.getReference().child(REPORT_COLLECTION).child(reportedUserKey.split("@")[0]). child(reporterUserKey.split("@")[0]);
        userReference.push().setValue(reasons);
    }


}
