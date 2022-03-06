package com.example.givetake.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MarkerOptions marker;
    private UiSettings mSettings;
    private Address lastUbi;
    private String email;
    Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("es"));

    // creating a variable
    // for search view.
    androidx.appcompat.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.fragment_maps);



        // initializing our search view.
        searchView = findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                Address address = null;
                // checking if  the entered location is null or not.
                if (location != null || location.equals("")) {
                    mMap.clear();
                    try {
                         List<Address> addresses = geocoder.getFromLocationName(location,0);
                         address= addresses.get(0);
                    } catch (IOException e) {
                        Toast.makeText(MapsFragment.this, "No se pudo conectar a internet", Toast.LENGTH_SHORT).show();
                    }
                    if (address==null) {
                        return false;
                    }
                    lastUbi=address;
                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    marker = new MarkerOptions().position(latLng).title(location);
                    mMap.addMarker(marker);

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                Address address = null;
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 0);
                    address = addresses.get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lastUbi=address;
                mMap.addMarker(new MarkerOptions().position(point));
            }
        });


        mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(true);
        mSettings.setRotateGesturesEnabled(true);

    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("No se puede añadir esta ubicación").setMessage("Actualmente nuestros servicios no soportan esta ubicación")
                .setPositiveButton("Aceptar",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void showAlertCountries(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("No se pueden añadir paises a tu lista").setMessage("Prueba una ubicación más específica")
                .setPositiveButton("Aceptar",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    */
}

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}