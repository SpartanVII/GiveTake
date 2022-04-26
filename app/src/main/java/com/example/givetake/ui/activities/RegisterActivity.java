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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Presenter presenter = new Presenter();
    private MarkerOptions marker;
    private Address lastAddress;
    private EditText birthDate;
    private String password;
    private GoogleMap mMap;
    private String email;
    private EditText name;
    private String gender;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    Geocoder geocoder;
    androidx.appcompat.widget.SearchView searchView;


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        password = bundle.getString("password");
        searchView = findViewById(R.id.registerSearchView);
        toolbar = findViewById(R.id.toolbarRegister);
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        setTitle(R.string.toolbar_title_register);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        geocoder  = new Geocoder(getApplicationContext(), new Locale("es"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.registerMap);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                if (location.equals("")) {
                    return false;
                }
                if (Geocoder.isPresent()) {
                    mMap.clear();
                    List<Address> addresses = new ArrayList<>();
                    try {
                        addresses = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        Toast.makeText(RegisterActivity.this, "No se pudo conectar a internet", Toast.LENGTH_SHORT).show();
                    }

                    if (addresses == null || addresses.isEmpty()) {
                        return false;
                    }

                    lastAddress = addresses.get(0);

                    LatLng latLng = new LatLng(lastAddress.getLatitude(), lastAddress.getLongitude());
                    marker = new MarkerOptions().position(latLng).title(location);
                    mMap.addMarker(marker);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                return  true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
        setup();
    }

    private void setup(){
        name = findViewById(R.id.registerName);
        dateSetup();
        buttonsSetup();
    }

    private void dateSetup(){
        birthDate = findViewById(R.id.registerFechaNac);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        birthDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
    }



    public void onRadioButtonClicked(View view) {
        switch(view.getId()) {
            case R.id.maleRegister:
                gender = "Hombre";
                break;
            case R.id.femaleRegister:
                gender = "Mujer";
                break;
            default:
                gender = "Otro";
        }
    }

    private void buttonsSetup(){
        Button confirmButton = findViewById(R.id.confirmRegister);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }



    private void signUp() {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                Date parsedDate =  new Date();
                                try {
                                    parsedDate = format.parse(birthDate.getText().toString());
                                }catch (Exception e){}
                                MyAddress myAddress = new MyAddress(lastAddress.getAddressLine(0),
                                        new LatLng(lastAddress.getLatitude(),lastAddress.getLongitude()));
                                User user1 = new User(name.getText().toString(), myAddress, email, gender, parsedDate);
                                user1.obtainAddressLine();
                                presenter.addUser(user1);
                                showHome();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Fallo de autentificación.", Toast.LENGTH_SHORT).show();
                            showAlert( "No te has podido registrar");
                        }
                    }
                });
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Error").setMessage(msg)
                .setPositiveButton("Aceptar",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showHome() throws InterruptedException {
        saveSession();
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("isRegistered","true");
        startActivity(homeIntent);
    }

    public void saveSession(){
        //Saving the session
        @SuppressLint("CommitPrefEdits")
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("email", email);
        prefsEditor.apply();
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
            searchView.setQuery("Debe poner su dirección", false);
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
                if (Geocoder.isPresent()){
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
                    lastAddress =addresses.get(0);
                    mMap.addMarker(new MarkerOptions().position(point));
                }
            }
        });

        UiSettings mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(true);
        mSettings.setRotateGesturesEnabled(true);

    }
}
