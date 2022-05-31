package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
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
    private Product oldOroduct;
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
        setTitle(R.string.toolbar_title_add_product);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Button confirmButton = findViewById(R.id.confirmProduct);
        confirmButton.setOnClickListener(v -> addProduct());

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
            oldOroduct = presenter.getProduct(productKey);
            name.setText(oldOroduct.getTitle());
            desc.setText(oldOroduct.getDescription());
            spinner.setSelection(adapter.getPosition(oldOroduct.getTag()));
            Glide.with(getApplicationContext()).load(oldOroduct.getImg()).into(imageView);
            modifying = true;
        }

        chooseBtn.setOnClickListener(v -> chooseImage());
        cameraBtn.setOnClickListener(v -> dispatchTakePictureIntent());
        cameraBtn.setEnabled(false);
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
        if (data == null) return;
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

        Product newproduct = new Product();
        newproduct.setTitle(name.getText().toString());
        newproduct.setDescription(desc.getText().toString());
        newproduct.setOwner(email.split("@")[0]);
        newproduct.setTag(spinner.getSelectedItem().toString());

        if (changedImage){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            if (modifying){
                String deleteImgUrl = "gs://givetake-9f7af.appspot.com/images/" + oldOroduct.getImg().split("images%")[1].split("\\?alt=media")[0];
                storageReference.child(deleteImgUrl).delete();
            }
            String url = "images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(url);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getTask().getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            newproduct.setImg(task.getResult().toString());
                            if (bundle!=null){
                                newproduct.setId(oldOroduct.getId());
                                presenter.modifyProduct(oldOroduct, newproduct);
                            }else {
                                presenter.addProduct(newproduct);
                            }
                        }
                    });
                }
            });
        }
        else{
            newproduct.setImg(oldOroduct.getImg());
            if (bundle!=null){
                newproduct.setId(oldOroduct.getId());
                presenter.modifyProduct(oldOroduct, newproduct);
            }else {
                presenter.addProduct(newproduct);
            }
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
