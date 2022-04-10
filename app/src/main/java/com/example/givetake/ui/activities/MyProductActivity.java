package com.example.givetake.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.givetake.R;
import com.example.givetake.model.MyAddress;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class MyProductActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Presenter presenter;
    private String productKey;
    private Toolbar toolbar;
    private TextView productName;
    private TextView productDesc;
    private TextView vendorAddress;
    private MyAddress vendorAddres;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        toolbar = findViewById(R.id.toolbarMyProduct);
        productName = findViewById(R.id.productNameMy);
        productDesc = findViewById(R.id.productDescMy);
        vendorAddress = findViewById(R.id.addressVendorMyProduct);
        presenter = new Presenter();
        setSupportActionBar(toolbar);
        setTitle("Información del producto");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            productKey = bundle.getString("productKey");
        }

        Product product = presenter.getProduct(productKey);
        User vendor = presenter.getUser(product.getOwner());
        System.out.println(vendor.getAddressToString());

        productName.setText(product.getTitle());
        productDesc.setText(product.getDescription());
        vendorAddress.setText(vendor.getAddressToString());
        vendorAddres = vendor.getAddress();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.productMyMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap =googleMap;
        LatLng latLng = vendorAddres.getLatLng();
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .strokeWidth(1)
                .strokeColor(Color.argb(200, 30, 178, 255))
                .fillColor(Color.argb(120, 102, 178, 255))
                .clickable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        UiSettings mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(false);
        mSettings.setRotateGesturesEnabled(false);
        mSettings.setScrollGesturesEnabled(false);

    }
}