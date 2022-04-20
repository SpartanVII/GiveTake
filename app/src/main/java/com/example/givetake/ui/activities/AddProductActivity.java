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
import android.os.Environment;
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
import androidx.core.content.FileProvider;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Button cameraBtn;
    private ImageView imageView;
    private boolean changedImage;
    private boolean modifying;

    private final int PICK_IMAGE_REQUEST = 71;
    private final int REQUEST_TAKE_PHOTO = 1;



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
        cameraBtn = findViewById(R.id.btnCamera);
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
        cameraBtn.setOnClickListener(v -> dispatchTakePictureIntent());
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
        filePath = data.getData();
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK  && data.getData() != null ) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        changedImage = true;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();  // Create the File where the photo should go
            } catch (IOException ex) {  // Error occurred while creating the File
                System.out.println("Error creando ruta");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "$PACKAGE$.provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        System.out.println("Error no hay camara");
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
        }
        else {
            this.desc.setError(null);
        }

        if (filePath==null && !modifying){
            //AlertDialog.Builder alertBuil
            valid = false;
        }

        return valid;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
