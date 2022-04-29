package com.example.givetake.ui.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.Review;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;

import java.sql.Date;
import java.time.Instant;
import java.util.Objects;

public class CompleteReviewActivity extends AppCompatActivity {
    private Presenter presenter;
    private ImageView imgReview;
    private Button completeBtn;
    private EditText otherProductName;
    private  EditText otherPerson;
    private EditText extraCost;
    private EditText comentary;
    private String mail;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new Presenter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_review);

        Toolbar toolbar = findViewById(R.id.toolbarCompleteReview);
        setSupportActionBar(toolbar);
        setTitle(R.string.toolbar_title_review);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String name = prefs.getString("name",null);
        mail = prefs.getString("email",null);

        imgReview = findViewById(R.id.imgProductReview);
        completeBtn = findViewById(R.id.completeRerviewButton);
        TextView productName = findViewById(R.id.productNameReview);
        otherProductName = findViewById(R.id.otherProductName);
        otherPerson = findViewById(R.id.otherPersonReview);
        extraCost = findViewById(R.id.extraCostReview);
        comentary = findViewById(R.id.comentaryReview);
        Spinner spinner = findViewById(R.id.scoreOptionsReview);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage("Una vez completada no podrás modificar tu reseña");
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.alert_dialog_negative_review,null);


        Bundle bundle = getIntent().getExtras();
        Product product = (Product) bundle.get("product");
        Review uncompleteReview = (Review) bundle.get("review");
        if (product!=null){
            Glide.with(getApplicationContext()).load(product.getImg()).centerCrop().into(imgReview);
            productName.setText(product.getTitle());
            completeBtn.setOnClickListener(v -> {
                user = presenter.getUser(otherPerson.getText().toString());
                Review review = new Review();
                if (!validateForm()){
                    return;
                }

                review.setAuthorName(name);
                review.setReviwedName(user.getName());
                review.setComentary(comentary.getText().toString());
                review.setProductName(productName.getText().toString());
                review.setOtherProductName(otherProductName.getText().toString());
                review.setProductImg(product.getImg());
                review.setReviewDate(Date.from(Instant.now()));
                review.setExtraPrice(Integer.parseInt(extraCost.getText().toString()));
                review.setCompleted(true);
                review.setScore(5);

                builder.setPositiveButton(R.string.alert_dialog_positive_review, (dialog, which) -> {
                    presenter.addReviewToMe(review, mail);
                    presenter.addReviewToTheOther(review, user.getMail());
                    presenter.swapProduct(product);
                    showHome();
                });
                builder.show();
            });
        }
        else {
            Glide.with(getApplicationContext()).load(uncompleteReview.getProductImg()).centerCrop().into(imgReview);
            productName.setText(uncompleteReview.getProductName());
            otherProductName.setText(uncompleteReview.getOtherProductName());
            otherProductName.setEnabled(false);
            otherPerson.setText(uncompleteReview.getReviwedName());
            otherPerson.setEnabled(false);
            extraCost.setText(""+uncompleteReview.getExtraPrice());
            extraCost.setEnabled(false);
            uncompleteReview.setCompleted(true);

            completeBtn.setOnClickListener(v -> {
                uncompleteReview.setScore(5);
                uncompleteReview.setComentary(comentary.getText().toString());

                builder.setPositiveButton(R.string.alert_dialog_positive_review, (dialog, which) -> {
                    presenter.addReviewToMe(uncompleteReview, mail);
                    showHome();
                });
                builder.show();
            });
        }

    }

    public void showHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean validateForm(){
        boolean valid = true;
        if (otherPerson.getText().toString().isEmpty()){
            otherPerson.setError("Obligatorio");
            valid = false;
        }
        if (otherPerson.getText().toString().equals(mail)){
            otherPerson.setError("Este correo te pertenece");
            valid = false;
        }
        if (user == null){
            otherPerson.setError("Este usuario no existe");
            valid = false;;
        }
        return valid;
    }
}