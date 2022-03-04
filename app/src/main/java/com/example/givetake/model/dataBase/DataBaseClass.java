package com.example.givetake.model.dataBase;

public class DataBaseClass {/*
    private final FirebaseFirestore db;
    Gson gson;


    public DataBaseManager() {
        this.db = FirebaseFirestore.getInstance();;
        this.gson = new Gson();
    }

    public void recover(String email){
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

    }

    public void save(String email){
        Map<String, String> map = new HashMap<>();
        map.put("LManager", gson.toJson(Singleton.getLocationManager().getLocations()));
        db.collection("users").document(email).set(map);
    }*/
}
