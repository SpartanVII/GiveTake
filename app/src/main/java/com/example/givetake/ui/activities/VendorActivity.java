package com.example.givetake.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.givetake.R;
import com.example.givetake.presenter.Presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VendorActivity extends AppCompatActivity {
    private Presenter presenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        toolbar = findViewById(R.id.toolbarVendorProfile);
        setSupportActionBar(toolbar);
        setTitle(R.string.toolbar_title_vendor_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        presenter = new Presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vendor_profile_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if (item.getTitle() != null){
            final String[] reportOptions =  getResources().getStringArray(R.array.report_options);
            final boolean[] checkedOptions = new boolean[reportOptions.length];
            Arrays.fill(checkedOptions, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(R.string.alert_dialog_title_report);
            builder.setMultiChoiceItems(reportOptions, checkedOptions, (dialog, which, isChecked) -> {
                checkedOptions[which] = isChecked;
            });

            builder.setPositiveButton(R.string.alert_dialog_report_confirm, (dialog, which) -> {
                    List<String> reportReasons = new ArrayList<>();
                    for (int i = 0; i < reportOptions.length  ; i++) {
                        if (checkedOptions[i]) reportReasons.add(reportOptions[i]);
                    }
                    if (!reportReasons.isEmpty()){
                    presenter.sendReport(reportReasons, getIntent().getExtras().getString("vendorKey"), email);
                        Toast.makeText(this, R.string.toast_sended_report, Toast.LENGTH_LONG).show();
                    }
            });
            builder.setNegativeButton(R.string.alert_dialog_report_cancel, null);
            builder.show();
        }
        else onNavigateUp();

        return true;
    }
}