package com.example.givetake.model.dataBase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataBaseClass {
    private final FirebaseFirestore db;
    private final DatabaseReference databaseReference;
    Gson gson;


    public DataBaseClass() {
        this.db = FirebaseFirestore.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
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
}
