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

import java.util.Objects;


public class  AddProductActivity extends AppCompatActivity {
    private Presenter presenter = new Presenter();
    private String email;
    private EditText name;
    private EditText desc;
    private Spinner spinner;
    private Toolbar toolbar;
    private Bundle bundle;
    private Product modifyProduct;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstnceState){
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_new_product);

        toolbar = findViewById(R.id.toolbarAddProduct);
        setSupportActionBar(toolbar);
        setTitle("Añadir producto");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category_add_product, R.layout.support_simple_spinner_dropdown_item );
        spinner.setAdapter(adapter);

        bundle = getIntent().getExtras();
        if(bundle != null) {
            String productKey = bundle.getString("productKey");
            modifyProduct = presenter.getProduct(productKey);
            name.setText(modifyProduct.getTitle());
            desc.setText(modifyProduct.getDescription());
            spinner.setSelection(adapter.getPosition(modifyProduct.getTag()));
        }
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

        if (bundle!=null && !modifyProduct.getTag().equals(product.getTag())){
                presenter.deleteProduct(modifyProduct);
        }
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
            this.desc.setError("Obligatorio");
            valid = false;
        }
        else if (desc.length()<60) {
            this.desc.setError("Mínimo 60 carácteres");
            valid = false;
        }
        else if (desc.length()>600) {
            this.desc.setError("Máximo 600 carácteres");
            valid = false;
        }/*
        else if (desc.split(" ").length>7) {
        this.desc.setError("Usa palabras por favor");
        valid = false;
        }
        */
        else {
            this.desc.setError(null);
        }

        return valid;
    }
}
