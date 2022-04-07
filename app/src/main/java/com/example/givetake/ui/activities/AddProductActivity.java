package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;


public class AddProductActivity extends AppCompatActivity {
    private Presenter presenter = new Presenter();
    private String email;
    private EditText name;
    private EditText desc;
    private Spinner spinner;
    private Toolbar toolbar;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_new_product);

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

        spinner = findViewById(R.id.spinnerProduct);
        name = findViewById(R.id.addProductName);
        desc = findViewById(R.id.addProductDesc);
        toolbar = findViewById(R.id.toolbarAddProduct);

        setSupportActionBar(toolbar);
        setTitle("Añadir producto");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category_add_product, R.layout.support_simple_spinner_dropdown_item );
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
        product.setTag(spinner.getSelectedItem().toString());
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

        String desc = this.desc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            this.name.setError("Obligatorio");
            valid = false;
        } else {
            this.name.setError(null);
        }

        return valid;
    }
}