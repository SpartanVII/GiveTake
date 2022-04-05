package com.example.givetake.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.MainActivity;


public class AddProductActivity extends AppCompatActivity {
    private String email;
    private EditText name;
    private EditText desc;
    private Spinner spinner;
    private Presenter presenter;
    Context appContext;
    androidx.appcompat.widget.SearchView searchView;


    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_add_producr);
        //setSupportActionBar(findViewById(R.layout.app_bar_main));

        Button confirmButton = findViewById(R.id.confirmProduct);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        Button cancelButton = findViewById(R.id.cancelProduct);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHome();
            }
        });

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        email = prefs.getString("email", null);

        spinner = findViewById(R.id.spinner);
        name = findViewById(R.id.addProductName);
        desc = findViewById(R.id.addProductDesc);
        presenter = new Presenter();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.support_simple_spinner_dropdown_item );
        spinner.setAdapter(adapter);
    }



    private void addProduct() {
        if (!validateForm()) {
            return;
        }
        Product product = new Product();
        product.setTitle(name.getText().toString());
        product.setDescription(desc.getText().toString());
        product.setOwner(email.split("@")[0]);
        presenter.addProduct(product);
        showHome();
    }


    public void showHome() {
        Intent homeIntent = new Intent(this, MainActivity.class);
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



        return valid;
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(2).setEnabled(false);
        menu.getItem(0).setVisible(false);
        menu.getItem(2).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_logged));

        return true;
    }

     */

}
