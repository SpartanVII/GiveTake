package com.example.givetake.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class MyProductActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Presenter presenter;
    private String productKey;
    private MyAddress vendorAddres;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        Toolbar toolbar = findViewById(R.id.toolbarMyProduct);
        TextView productName = findViewById(R.id.productNameMy);
        TextView productDesc = findViewById(R.id.productDescMy);
        TextView vendorAddress = findViewById(R.id.addressVendorMyProduct);
        ImageView productImg = findViewById(R.id.imgProductrMyProduct);
        Button swapBtn =findViewById(R.id.swapedButton);
        presenter = new Presenter();

        setSupportActionBar(toolbar);
        setTitle(R.string.toolbar_title_info_product);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        product = presenter.getProduct(getIntent().getExtras().getString("productKey"));
        User vendor = presenter.getUser(product.getOwner());

        Glide.with(getApplicationContext()).load(product.getImg()).centerCrop().into(productImg);
        productName.setText(product.getTitle());
        productDesc.setText(product.getDescription());
        vendorAddress.setText(vendor.obtainAddressLine());
        vendorAddres = vendor.getAddress();

        swapBtn.setOnClickListener(v -> {
            /*
            Intent intent = new Intent(this, );
            intent.putExtra("productKey",productKey);
            startActivity(intent);
             */
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.productMyMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = vendorAddres.getLatLng();
        googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(200)
                .strokeWidth(1)
                .strokeColor(Color.argb(200, 30, 178, 255))
                .fillColor(Color.argb(120, 102, 178, 255))
                .clickable(true));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        UiSettings mSettings = googleMap.getUiSettings();
        mSettings.setZoomControlsEnabled(false);
        mSettings.setRotateGesturesEnabled(false);
        mSettings.setScrollGesturesEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_product_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == null){
            onNavigateUp();
        }
        else if (item.getTitle().equals("Borrar producto")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Borrar producto");
            builder.setMessage("¿Está seguro de que quieres eliminarlo?");
            builder.setPositiveButton(R.string.alert_dialog_positive,
                    (dialog, which) -> {
                        presenter.deleteProduct(product);
                        presenter.deleteImage(product.getImg());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    });
            builder.setNegativeButton(R.string.alert_dialog_negative, null);
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