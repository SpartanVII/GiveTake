package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


public class  AddProductActivity extends AppCompatActivity {
    private Presenter presenter = new Presenter();
    private String email;
    private EditText name;
    private EditText desc;
    private Spinner spinner;
    private Toolbar toolbar;
    private Bundle bundle;
    private Product modifyProduct;
    private Uri filePath;
    private Button chooseBtn;
    private ImageView imageView;
    private boolean changedImage;
    private boolean modifying;
    private final int PICK_IMAGE_REQUEST = 71;


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

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        email = prefs.getString("email", null);

        spinner = findViewById(R.id.spinnerProduct);
        name = findViewById(R.id.addProductName);
        desc = findViewById(R.id.addProductDesc);
        chooseBtn = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.imgAddProduct);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category_add_product, R.layout.support_simple_spinner_dropdown_item );
        spinner.setAdapter(adapter);
        modifying = false;
        changedImage = false;
        bundle = getIntent().getExtras();
        if(bundle != null) {
            setTitle("Modificar producto");
            String productKey = bundle.getString("productKey");
            modifyProduct = presenter.getProduct(productKey);
            name.setText(modifyProduct.getTitle());
            desc.setText(modifyProduct.getDescription());
            spinner.setSelection(adapter.getPosition(modifyProduct.getTag()));
            Glide.with(getApplicationContext()).load(modifyProduct.getImg()).into(imageView);
            modifying = true;
        }

        chooseBtn.setOnClickListener(v -> chooseImage());

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Elija una foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                changedImage = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
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

        if (changedImage){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            String url = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(url);
            try {
                UploadTask upload = ref.putFile(filePath);
                while (!upload.isComplete()){}
                Task<Uri> downloadUrl = upload.getResult().getStorage().getDownloadUrl();
                while (!downloadUrl.isComplete()){}
                product.setImg(downloadUrl.getResult().toString());
            }catch (Exception e){e.printStackTrace();}


        }else product.setImg(modifyProduct.getImg());

        if (bundle!=null){
            product.setId(modifyProduct.getId());
            presenter.deleteProduct(modifyProduct);
            presenter.modifyProduct(product);
        }else {
            presenter.addProduct(product);
        }
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

        if (filePath==null && !modifying){
            //AlertDialog.Builder alertBuil
            valid = false;
        }

        return valid;
    }
}
