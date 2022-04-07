package com.example.givetake.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;

public class InfoProductActivity extends AppCompatActivity {
    private Presenter presenter;
    private String productKey;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);

        toolbar = findViewById(R.id.toolbarInfoProduct);
        presenter = new Presenter();

        setSupportActionBar(toolbar);
        setTitle("Información del producto");

        if(savedInstanceState!=null){
            productKey = savedInstanceState.get("productKey").toString();
        }
        Product product = presenter.getProduct(productKey);
        User vendor = presenter.getUser(product.getOwner());



    }
}