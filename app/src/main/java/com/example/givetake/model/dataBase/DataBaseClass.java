package com.example.givetake.model.dataBase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.givetake.model.manager.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataBaseClass {
    private final FirebaseDatabase db;

    Gson gson;


    public DataBaseClass() {
        this.db = FirebaseDatabase.getInstance();
        this.gson = new Gson();
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

    public void save(String email){
        /*
        Map<String, String> map = new HashMap<>();
        map.put("LManager", gson.toJson(Singleton.getLocationManager().getLocations()));
        db.collection("users").document(email).set(map);

         */
    }

    public void initialy(UserManager userManager){
        DatabaseReference databaseReference = db.getReference("Users");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    System.out.println(task.getResult().getValue());
                    System.out.println("lala");
                }
            }
        });

    }
}
