package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EditText name;
    private EditText birthDate;
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
        gender = user.obtainGenderToString();
        myAddress = user.getAddress();
        searchView.setQuery(user.obtainAddressLine(), false);

        geocoder  = new Geocoder(getApplicationContext(), new Locale("es"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.editMap);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = searchView.getQuery().toString();
                if (!location.equals("")) {
                    mMap.clear();
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        Toast.makeText(EditProfileActivity.this, R.string.toast_internet_conection, Toast.LENGTH_SHORT).show();
                    }

                    if (addresses == null || addresses.isEmpty()){
                        return false;
                    }

                    lastAddress =addresses.get(0);
                    LatLng latLng = new LatLng(lastAddress.getLatitude(), lastAddress.getLongitude());
                    marker = new MarkerOptions().position(latLng).title(location);
                    mMap.addMarker(marker);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
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
        buttonsSetup();
        genderSetup();
    }

    public void genderSetup(){
        RadioButton male = findViewById(R.id.maleEdit);
        RadioButton female = findViewById(R.id.femaleEdit);
        RadioButton other = findViewById(R.id.otherEdit);

        if (user.getGender().equals("Hombre"))
            male.setChecked(true);
        else if (user.getGender().equals("Mujer"))
            female.setChecked(true);
        else other.setChecked(true);
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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        birthDate.setText(format.format(user.getBirth()));
    }


    private void buttonsSetup(){
        Button confirmButton = findViewById(R.id.confirmEdit);
        confirmButton.setOnClickListener(v -> modifyUser());
    }


    private void modifyUser() {
        if (!validateForm()) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate =  new Date();
        try {
            parsedDate = format.parse(birthDate.getText().toString());
        }catch (Exception e){}
        MyAddress myAddress = new MyAddress(lastAddress.getAddressLine(0),
                new LatLng(lastAddress.getLatitude(),lastAddress.getLongitude()));
        user.setAddress(myAddress);
        user.setName(name.getText().toString());
        user.setGender(user.fromStrToGender(gender));
        user.setBirth(parsedDate);
        presenter.addUser(user);
        showHome();
    }


    public void showHome() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("name", user.getName()).apply();
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("isRegistered","true");
        homeIntent.putExtra("email", email);
        startActivity(homeIntent);
    }

    public void onRadioButtonClicked(View view) {
        switch(view.getId()) {
            case R.id.maleEdit:
                gender = "Hombre";
                break;
            case R.id.femaleEdit:
                gender = "Mujer";
                break;
            default:
                gender = "Otro";
        }
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

        UiSettings mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(true);
        mSettings.setRotateGesturesEnabled(true);

        searchView.setQuery(user.obtainAddressLine(), true);
        searchView.setQuery(" ",false);
    }

    /*
    private class MyTask extends AsyncTask<String, Integer, Address>{

        @Override
        protected Address doInBackground(String... strings) {
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocationName(strings[0],1);
            } catch (IOException e) {
                Toast.makeText(EditProfileActivity.this, "No se pudo conectar a internet", Toast.LENGTH_SHORT).show();
            }
            if(addresses.isEmpty() || addresses==null)  return null;
            return addresses.get(0);
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            if (address == null){
                return;
            }

            lastAddress =address;
            LatLng latLng = new LatLng(lastAddress.getLatitude(), lastAddress.getLongitude());
            marker = new MarkerOptions().position(latLng);
            mMap.addMarker(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        }
    }

     */
}
