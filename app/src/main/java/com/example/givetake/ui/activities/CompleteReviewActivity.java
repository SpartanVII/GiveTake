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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private EditText otherProductName;
    private TextView otherProductNameFixed;
    private  EditText otherPerson;
    private  TextView otherPersonFixed;
    private EditText extraCost;
    private TextView extraCostFixed;
    private TextView productName;
    private EditText comentary;
    private Spinner spinner;
    private String mail;
    private String name;
    private Product product;
    private Review uncompleteReview;
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
        name = prefs.getString("name",null);
        mail = prefs.getString("email",null);

        Button completeBtn = findViewById(R.id.completeRerviewButton);
        imgReview = findViewById(R.id.imgProductReview);
        productName = findViewById(R.id.productNameReview);
        otherProductName = findViewById(R.id.otherProductName);
        otherProductNameFixed = findViewById(R.id.otherProductNameFixed);
        otherPerson = findViewById(R.id.otherPersonReview);
        otherPersonFixed = findViewById(R.id.otherPersonReviewFixed);

        extraCost = findViewById(R.id.extraCostReview);
        extraCostFixed = findViewById(R.id.extraCostReviewFixed);

        comentary = findViewById(R.id.comentaryReview);
        spinner = findViewById(R.id.scoreOptionsReview);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_review_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage("Una vez completada tu reseña no la podrás modificar");
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.alert_dialog_negative_review,null);


        Bundle bundle = getIntent().getExtras();
        product = (Product) bundle.get("product");
        uncompleteReview = (Review) bundle.get("review");
        if (product!=null){
            setupFirstReview();
            completeBtn.setOnClickListener(v -> {
                Review review = createFirstReview();
                if (review == null) return;
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
            setupSecondReview();
            completeBtn.setOnClickListener(v -> {
                uncompleteReview.setScoreFromExperience(spinner.getSelectedItem().toString());
                uncompleteReview.setComentary(comentary.getText().toString());

                builder.setPositiveButton(R.string.alert_dialog_positive_review, (dialog, which) -> {
                    presenter.addReviewToMe(uncompleteReview, mail);
                    showHome();
                });
                builder.show();
            });
        }

    }
    public void setupSecondReview(){
        otherPerson.setVisibility(View.GONE);
        otherProductName.setVisibility(View.GONE);
        extraCost.setVisibility(View.GONE);
        extraCostFixed.setVisibility(View.VISIBLE);

        spinner.setSelection(2);
        Glide.with(getApplicationContext()).load(uncompleteReview.getProductImg()).centerCrop().into(imgReview);
        productName.setText(uncompleteReview.getProductName());
        otherProductNameFixed.setText(uncompleteReview.getOtherProductName());
        otherPersonFixed.setText(uncompleteReview.getReviwedName());
        extraCostFixed.setText(""+uncompleteReview.getExtraPrice());
        uncompleteReview.setCompleted(true);
    }

    public void setupFirstReview(){
        Glide.with(getApplicationContext()).load(product.getImg()).centerCrop().into(imgReview);
        productName.setText(product.getTitle());
        otherPersonFixed.setVisibility(View.GONE);
        otherProductNameFixed.setVisibility(View.GONE);
        extraCostFixed.setVisibility(View.GONE);

        spinner.setSelection(2);
    }

    public Review createFirstReview(){
        user = presenter.getUser(otherPerson.getText().toString());
        Review review = new Review();
        if (!validateForm()){
            return null;
        }

        review.setAuthorName(name);
        review.setReviwedName(user.getName());
        review.setComentary(comentary.getText().toString());
        review.setProductName(productName.getText().toString());
        review.setOtherProductName(otherProductName.getText().toString());
        review.setProductImg(product.getImg());
        review.setReviewDate(Date.from(Instant.now()));
        String extraCosto = "0";
        if (!extraCost.getText().toString().isEmpty()) extraCosto = extraCost.getText().toString();
        review.setExtraPrice(Integer.parseInt(extraCosto));
        review.setCompleted(true);
        review.setScoreFromExperience(spinner.getSelectedItem().toString());
        return review;
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