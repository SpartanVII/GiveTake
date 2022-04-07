package com.example.givetake.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;

public class InfoProductActivity extends AppCompatActivity {
    private Presenter presenter;
    private String productKey;
    private Toolbar toolbar;
    private TextView productName;
    private TextView productDesc;
    private TextView vendorName;
    private TextView vendorNote;
    private TextView vendorAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_product);

        toolbar = findViewById(R.id.toolbarInfoProduct);
        productName = findViewById(R.id.productNameInfo);
        productDesc = findViewById(R.id.productDescInfo);
        vendorName = findViewById(R.id.vendorNameInfoProduct);
        vendorNote = findViewById(R.id.replaceWithTheVendorNote);
        vendorAddress = findViewById(R.id.addressVendorInfoProduct);
        presenter = new Presenter();
        setSupportActionBar(toolbar);
        setTitle("Información del producto");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            productKey = bundle.getString("productKey");
        }
        Product product = presenter.getProduct(productKey);
        User vendor = presenter.getUser(product.getOwner());

        productName.setText(product.getTitle());
        productDesc.setText(product.getDescription());
        vendorName.setText(vendor.getName());
        vendorNote.setText(""+vendor.getGlobalScore());
        vendorAddress.setText(vendor.getAddressToString());


    }
}