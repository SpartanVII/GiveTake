package com.example.givetake.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givetake.R;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EditText name;
    private EditText birthDate;
    private AutoCompleteTextView autoCompleteGender;
    private Presenter presenter;
    private Button confirmButton;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private MarkerOptions marker;
    private UiSettings mSettings;
    private Address lastAddress;

    Geocoder geocoder;
    Context appContext;
    androidx.appcompat.widget.SearchView searchView;


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        password = bundle.getString("password");
        presenter = new Presenter();
        searchView = findViewById(R.id.idSearchView);

        mAuth = FirebaseAuth.getInstance();
        appContext = getApplicationContext();

        geocoder  = new Geocoder(getApplicationContext(), new Locale("es"));
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
                // checking if  the entered location is null or not.
                if (location == null || location.equals("")) {
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
        name = findViewById(R.id.usuNombre);
        dateSetup();
        genderSetup();
        buttonsSetup();
    }

    private void dateSetup(){
        birthDate = findViewById(R.id.usuFechaNac);
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

    @SuppressLint("SetTextI18n")
    private void genderSetup(){
        //Selects
        String[] selectGender = {"Hombre", "Mujer", "Otro"};
        ArrayAdapter<String> adapterGender;

        autoCompleteGender = findViewById(R.id.genderSelect);
        adapterGender = new ArrayAdapter<>(this, R.layout.select_item,selectGender);
        autoCompleteGender.setAdapter(adapterGender);
        autoCompleteGender.setText("Otro",false);

    }

    private void buttonsSetup(){
        confirmButton = findViewById(R.id.confirmEdit);
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
                                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate parsedDate = LocalDate.parse(birthDate.getText().toString(), format);
                                presenter.addUser(
                                        new User(name.getText().toString(), lastAddress, email, autoCompleteGender.getText().toString(), parsedDate));
                                showHome();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(appContext, "Fallo de autentificación.", Toast.LENGTH_SHORT).show();
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
        /*
        TODO Validar la edad
        TODO Aqui validar la ubicación

        if (password.length()<6){
            pass.setError("La contraseña debe tener mínimo 6 caracteres");
            showAlert("La contraseña debe tener mínimo 6 caracteres");
            valid = false;
        }*/

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

        mSettings = mMap.getUiSettings();
        mSettings.setZoomControlsEnabled(true);
        mSettings.setRotateGesturesEnabled(true);

    }
}
