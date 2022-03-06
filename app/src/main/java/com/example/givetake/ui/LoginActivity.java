package com.example.givetake.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givetake.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button signInButton;
    Button signUpButton;
    String mail;
    String password;
    EditText email;
    EditText pass;
    Context appContext;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.emailSignInButton);
        signUpButton = findViewById(R.id.emailSignUpButton);

        email = findViewById(R.id.fieldEmail);
        pass = findViewById(R.id.fieldPassword);
        mAuth = FirebaseAuth.getInstance();

        appContext=getApplicationContext();
        setup();
    }

    @SuppressLint("ResourceType")
    public void onStart() {
        super.onStart();
        findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
    }




    public void setup(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(),pass.getText().toString());
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email.getText().toString(),pass.getText().toString());
            }
        });
    }


    private void signIn(String email, String pass) {
        if (!validateForm()) {
            return;
        }
        mail=email;
        password=pass;
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            try {
                                showHome();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(appContext, "Fallo de autentificación.", Toast.LENGTH_SHORT).show();
                            showAlert("Se ha producido un error en la autentificación de usuario");
                        }
                    }
                });
    }

    private void signUp(String email, String pass){
        if (!validateForm()) {
            return;
        }
        mail=email;
        password= pass;
        try {
            showRegister();
        }catch (Exception e){
            e.printStackTrace();
        }
        /*
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            try {
                                showRegister();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(appContext, "Fallo de autentificación.", Toast.LENGTH_SHORT).show();
                            showAlert( "No te has podido registrar");
                        }
                    }
                });

         */
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
        invalidateOptionsMenu();
        homeIntent.putExtra("email", mail);
        homeIntent.putExtra("isRegistered","true");
        startActivity(homeIntent);
    }


    public void showRegister() throws InterruptedException {
        //Ir a otro layout y que te pida fechade nacimiento, direccion, genero...
        Intent homeIntent = new Intent(this, RegisterActivity.class);
        homeIntent.putExtra("email", mail);
        homeIntent.putExtra("password", password);
        startActivity(homeIntent);
    }


    private boolean validateForm() {
        boolean valid = true;

        String mail = email.getText().toString();
        if (TextUtils.isEmpty(mail)) {
            email.setError("Obligatorio");
            valid = false;
        } else {
            email.setError(null);
        }

        String password = pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }
        if (password.length()<6){
            pass.setError("La contraseña debe tener mínimo 6 caracteres");
            showAlert("La contraseña debe tener mínimo 6 caracteres");
            valid = false;
        }

        return valid;
    }
}
