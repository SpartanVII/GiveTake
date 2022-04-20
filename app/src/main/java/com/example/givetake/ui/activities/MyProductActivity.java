package com.example.givetake.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
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
    private ImageView productImg;
    private Product product;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        toolbar = findViewById(R.id.toolbarMyProduct);
        productName = findViewById(R.id.productNameMy);
        productDesc = findViewById(R.id.productDescMy);
        vendorAddress = findViewById(R.id.addressVendorMyProduct);
        productImg = findViewById(R.id.imgProductrMyProduct);
        presenter = new Presenter();
        setSupportActionBar(toolbar);
        setTitle("Información del producto");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            productKey = bundle.getString("productKey");
        }

        product = presenter.getProduct(productKey);
        User vendor = presenter.getUser(product.getOwner());

        Glide.with(getApplicationContext()).load(product.getImg()).centerCrop().into(productImg);
        productName.setText(product.getTitle());
        productDesc.setText(product.getDescription());
        vendorAddress.setText(vendor.obtainAddressLine());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myproduct, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            startActivity(new Intent(this, MainActivity.class));
        if (item.getTitle().equals("Borrar producto")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Borrar producto");
            builder.setMessage("¿Está seguro de que quieres eliminarlo?");
            builder.setNegativeButton("Si",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.deleteProduct(product);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setPositiveButton("No", null);
            builder.show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
            intent.putExtra("productKey",product.getId());
            startActivity(intent);
        }
        return true;
    }
}