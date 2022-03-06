package com.example.givetake.model.dataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.givetake.model.Product;
import com.example.givetake.model.singleton.ProductManagerSingleton;
import com.example.givetake.model.singleton.UserManagerSingleton;
import com.example.givetake.model.User;
import com.example.givetake.model.manager.ProductManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DataBaseClass {
    private final FirebaseDatabase db;
    Gson gson;


    public DataBaseClass() {
        this.db = FirebaseDatabase.getInstance("https://givetake-9f7af-default-rtdb.europe-west1.firebasedatabase.app/");
        this.gson = new Gson();
        db.goOnline();
    }

    public void recover(String email){
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Type type = new TypeToken<Map<String, Location>>(){}.getType();
        try {
            db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String json = (String) task.getResult().get("LManager");
                    if (json!= null){
                        Map<String, Location> locs = gson.fromJson( json, type);
                        Singleton.setLocationManager(locs,new GeoCodingClass());
                    }else {
                        Singleton.getLocationManager();
                    }
                }
            });
        }catch (NullPointerException  e){

        }
*/
    }

    public void save(User user){
        DatabaseReference userReference = db.getReference("Users").child(user.getMail());
        userReference.setValue(gson.toJson(user));

        DatabaseReference productReference = db.getReference("Products");
        productReference.setValue(gson.toJson(ProductManagerSingleton.getProductManager()));
    }

    public void initialy(){
        Type userType = new TypeToken<Map<String, User>>(){}.getType();
        DatabaseReference userReference = db.getReference("Users");
        userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String json = String.valueOf(task.getResult().getValue());
                    UserManagerSingleton.setUserManager( gson.fromJson(json, userType));
                }
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                String json = String.valueOf(dataSnapshot.getValue());
                UserManagerSingleton.setUserManager(gson.fromJson(json, userType));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("realtime", "Failed to read value.", error.toException());
            }
        });

        Type productType = new TypeToken<Map<String, List<Product>>>(){}.getType();
        DatabaseReference prductReference = db.getReference("Products");
        prductReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String json = String.valueOf(task.getResult().getValue());
                    UserManagerSingleton.setUserManager( gson.fromJson(json, productType));
                }
            }
        });

        prductReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                String json = String.valueOf(dataSnapshot.getValue());
                UserManagerSingleton.setUserManager(gson.fromJson(json, productType));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("realtime", "Failed to read value.", error.toException());
            }
        });

    }


}
