package com.example.givetake.model.dataBase;

import android.util.Log;

import com.example.givetake.model.json.LocalDateAdapter;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

public class DataBaseClass {
    private final FirebaseDatabase db;
    Gson gson;


    public DataBaseClass() {
        this.db = FirebaseDatabase.getInstance("https://givetake-9f7af-default-rtdb.europe-west1.firebasedatabase.app/");
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        db.goOnline();
    }

    public void save(User user){
            DatabaseReference userReference = db.getReference("Users").child(user.getMail().split("@")[0]);
            userReference.setValue(gson.toJson(user));
    }



    public void deleteUser(String userKey){
        db.getReference("Users").child(userKey).removeValue();
    }

    public void initialy(){
        Type userType = new TypeToken<Map<String, User>>(){}.getType();
        DatabaseReference userReference = db.getReference("Users");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String json = String.valueOf(dataSnapshot.getValue());
                UserManagerSingleton.setUserManager(gson.fromJson(json, userType));
                ProductManagerSingleton.createProductManagerWithMap(gson.fromJson(json, userType));
                Log.w("Firebase", "Datos cargados");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

    }


}
