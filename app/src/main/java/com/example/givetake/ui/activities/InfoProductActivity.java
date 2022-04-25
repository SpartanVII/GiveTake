package com.example.givetake.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.MessageFormat;
import java.util.Objects;

public class InfoProductActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Presenter presenter;
    private String productKey;
    private String nextDestination;
    private Toolbar toolbar;
    private TextView productName;
    private TextView productDesc;
    private TextView vendorName;
    private TextView vendorNote;
    private TextView vendorAddress;
    private ImageView productImg;
    private ImageView favProduct;
    private MyAddress vendorAddres;
    private GoogleMap mMap;



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
        productImg = findViewById(R.id.imgProductrInfoProduct);
        favProduct = findViewById(R.id.addFav);
        presenter = new Presenter();
        setSupportActionBar(toolbar);
        setTitle("Información del producto");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            productKey = bundle.getString("productKey");
            nextDestination = bundle.getString("nextDestination");
        }

        Product product = presenter.getProduct(productKey);
        User vendor = presenter.getUser(product.getOwner());
        User user = presenter.getUser(email);
        productName.setText(product.getTitle());
        productDesc.setText(product.getDescription());
        vendorName.setText(vendor.getName());
        vendorNote.setText(MessageFormat.format("{0}", vendor.getGlobalScore()));
        vendorAddress.setText(vendor.obtainAddressLine());
        vendorAddres = vendor.getAddress();
        Glide.with(getApplicationContext()).load(product.getImg()).centerCrop().into(productImg);

        AnimatedVectorDrawableCompat toChecked = AnimatedVectorDrawableCompat.create(this, R.drawable.heart_unchecked_to_cheked);
        AnimatedVectorDrawableCompat toUnchecked = AnimatedVectorDrawableCompat.create(this, R.drawable.heart_checked_to_uncheked);

        if (presenter.isFavorite(product)) favProduct.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.heart_filled_vector));
        else favProduct.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.heart_unfilled_vector));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.productInforMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        favProduct.setOnClickListener(v -> {
            if (presenter.isFavorite(product)){
                favProduct.setImageDrawable(toUnchecked);
                Objects.requireNonNull(toUnchecked).start();
                presenter.deleteFavoriteProduct(product);
            }
            else{
                favProduct.setImageDrawable(toChecked);
                Objects.requireNonNull(toChecked).start();
                presenter.addFavoriteProduct(product);
            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        if (nextDestination!=null) {
            intent.putExtra("nextDestination", "favorites");
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}