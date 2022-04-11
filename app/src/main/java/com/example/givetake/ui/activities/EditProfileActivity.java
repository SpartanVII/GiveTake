package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.givetake.R;
import com.example.givetake.model.MyAddress;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EditText name;
    private EditText birthDate;
    private AutoCompleteTextView autoCompleteGender;
    private String gender;
    private Presenter presenter;
    private String email;
    private GoogleMap mMap;
    private MarkerOptions marker;
    private Address lastAddress;
    private MyAddress myAddress;
    private Toolbar toolbar;
    private User user;

    private Geocoder geocoder;
    private androidx.appcompat.widget.SearchView searchView;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            email = bundle.getString("email");
        }else {
            @SuppressLint("CommitPrefEdits")
            SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            email = prefs.getString("email", null);
        }

        presenter = new Presenter();
        searchView = findViewById(R.id.editSearchView);
        name = findViewById(R.id.editName);
        toolbar = findViewById(R.id.toolbarEditProfile);

        setSupportActionBar(toolbar);
        setTitle("Editar perfil");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        user = presenter.getUser(email.split("@")[0]);
        name.setText(user.getName());
        gender = user.getGenderToString();
        myAddress = user.getAddress();
        searchView.setQuery(user.getAddressToString(), false);



        geocoder  = new Geocoder(getApplicationContext(), new Locale("es"));
        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.editMap);

        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                if (location != null || location.equals("")) {
                    mMap.clear();
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        Toast.makeText(EditProfileActivity.this, "No se pudo conectar a internet", Toast.LENGTH_SHORT).show();
                    }

                    if (addresses== null || addresses.isEmpty()){
                        return false;
                    }

                    lastAddress =addresses.get(0);
                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(lastAddress.getLatitude(), lastAddress.getLongitude());
                    // on below line we are adding marker to that position.
                    marker = new MarkerOptions().position(latLng).title(location);
                    mMap.addMarker(marker);
                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }
                return  true;
                }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);
        setup();
    }

    private void setup(){
        name = findViewById(R.id.editName);
        dateSetup();
        genderSetup();
        buttonsSetup();
    }

    private void dateSetup(){
        birthDate = findViewById(R.id.editFechaNac);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        birthDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String monthStrin = month<10?"0"+month:month+"";
                                String dayStrin = dayOfMonth<10?"0"+dayOfMonth:dayOfMonth+"";

                                String date = dayStrin+"/"+monthStrin+"/"+year;
                                birthDate.setText(date);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        birthDate.setText(user.getBirth().format(format));
    }

    @SuppressLint("SetTextI18n")
    private void genderSetup(){
        //Selects
        String[] selectGender = {"Hombre", "Mujer", "Otro"};
        ArrayAdapter<String> adapterGender;

        autoCompleteGender = findViewById(R.id.editGenderSelect);
        adapterGender = new ArrayAdapter<>(this, R.layout.select_item, selectGender);
        autoCompleteGender.setAdapter(adapterGender);
        autoCompleteGender.setText(gender,false);

    }

    private void buttonsSetup(){
        Button confirmButton = findViewById(R.id.confirmEdit);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modificar el user en el service manager
                modifyUser();
            }
        });

        Button cancelButton = findViewById(R.id.cancelEdit);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });
    }


    private void modifyUser() {
        if (!validateForm()) {
            return;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(birthDate.getText().toString(), format);
        MyAddress myAddress = new MyAddress(lastAddress.getAddressLine(0),
                new LatLng(lastAddress.getLatitude(),lastAddress.getLongitude()));
        presenter.addUser(
                new User(name.getText().toString(), myAddress, email, autoCompleteGender.getText().toString(), parsedDate));
        showHome();

    }


    public void showHome() {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("isRegistered","true");
        startActivity(homeIntent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = this.name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            this.name.setError("Obligatorio");
            valid = false;
        } else {
            this.name.setError(null);
        }

        String gen = autoCompleteGender.getText().toString();
        if (TextUtils.isEmpty(gen)) {
            autoCompleteGender.setError("Obligatorio");
            valid = false;
        } else {
            autoCompleteGender.setError(null);
        }

        String date = birthDate.getText().toString();
        if (TextUtils.isEmpty(date)){
            birthDate.setError("Obligatorio");
            valid = false;
        } else {
            birthDate.setError(null);
        }
        LocalDate parsedDate;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            parsedDate = LocalDate.parse(date, formato);
        } catch (Exception e){
            birthDate.setError("Formato incorrecto");
            parsedDate = LocalDate.parse("01/12/1900", formato);
            valid=false;
        }

        Period period = Period.between(parsedDate, LocalDate.now());
        if (period.getYears()<18){
            birthDate.setError("Hay que tener 18 años mínimo");
            valid= false;
        }

        if(lastAddress==null){
            searchView.setQuery("Debe poner su dirección", true);
            valid=false;
        }

        return valid;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses== null || addresses.isEmpty()){
                    return ;
                }
                lastAddress = addresses.get(0);
                mMap.addMarker(new MarkerOptions().position(point));
            }
        });

        mMap.addMarker(new MarkerOptions().position(myAddress.getLatLng()));
        searchView.setQuery(user.getAddressToString(), true);


        UiSettings mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(true);
        mSettings.setRotateGesturesEnabled(true);

    }
}
