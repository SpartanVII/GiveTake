package com.example.givetake.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givetake.R;
import com.example.givetake.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private EditText nombre;
    private EditText fechaNac;
    private AutoCompleteTextView autoCompleteGenero;
    private Button confirmButton;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    Context appContext;

    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        password = bundle.getString("password");

        mAuth = FirebaseAuth.getInstance();
        appContext = getApplicationContext();

        setup();
    }

    private void setup(){
        nombre = findViewById(R.id.usuNombre);
        fechaSetup();
        generoSetup();
        confirmSetup();
    }

    private void fechaSetup(){
        fechaNac = findViewById(R.id.usuFechaNac);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        fechaNac.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                LocalDate fecha = LocalDate.of(year,month,dayOfMonth);
                                fechaNac.setText(fecha.toString());
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
    }

    private void generoSetup(){
        //Selects
        String[] selectGenero = {"Hombre","Mujer","Otro"};
        ArrayAdapter<String> adapterGenero;

        autoCompleteGenero = findViewById(R.id.generoSelect);
        adapterGenero = new ArrayAdapter<String>(this, R.layout.select_item,selectGenero);
        autoCompleteGenero.setAdapter(adapterGenero);
    }

    private void confirmSetup(){
        confirmButton = findViewById(R.id.confirmRegister);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signUp(email,password);
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = nombre.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nombre.setError("Obligatorio");
            valid = false;
        } else {
            nombre.setError(null);
        }

        String gen = autoCompleteGenero.getText().toString();
        if (TextUtils.isEmpty(gen)) {
            autoCompleteGenero.setError("Obligatorio");
            valid = false;
        } else {
            autoCompleteGenero.setError(null);
        }
        /*
        TODO Aqui validar la ubicación
        if (password.length()<6){
            pass.setError("La contraseña debe tener mínimo 6 caracteres");
            showAlert("La contraseña debe tener mínimo 6 caracteres");
            valid = false;
        }*/

        return valid;
    }

    private void signUp(String email, String password, String nombre, String direccion, String genero, LocalDate fechNacimiento) {
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
                                User usuario = new User(nombre, direccion, email, genero, fechNacimiento);
                                showHome(usuario);
                                //showRegister(user.getEmail());
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

    public void showHome(User user) throws InterruptedException {
        Intent homeIntent = new Intent(this, MainActivity.class);
        invalidateOptionsMenu();
        //homeIntent.putExtra("user", user);
        homeIntent.putExtra("isRegistered","true");
        startActivity(homeIntent);
    }

}
