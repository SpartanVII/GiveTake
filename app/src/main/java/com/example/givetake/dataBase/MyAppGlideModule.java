package com.example.givetake.dataBase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // Register FirebaseImageLoader to handle StorageReference
        //registry.append(StorageReference.class, InputStream.class,new FirebaseImageLoader);
        System.out.println("Cargaaaaaaaaaa");
    }
}
