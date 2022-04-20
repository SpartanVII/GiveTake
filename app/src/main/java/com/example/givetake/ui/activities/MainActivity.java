package com.example.givetake.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.givetake.R;
import com.example.givetake.presenter.Presenter;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.givetake.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private boolean isRegistered = false;
    private Presenter presenter = new Presenter();
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        try {
            session();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isRegistered){
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_profile, R.id.nav_favs, R.id.nav_settings)
                    .setOpenableLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }

    }

    @SuppressLint("ResourceType")
    public void session() throws InterruptedException {
        @SuppressLint("CommitPrefEdits")
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if(email!=null){
            isRegistered=true;
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(2).setEnabled(false);

        if (!isRegistered){
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_logout));
        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_logged));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String option = (String) item.getTitle();
        if (option==null){
            onSupportNavigateUp();
            return true;
        }
        if (option.equals("Iniciar Sesión")){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);

        }
        else {
            SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
            prefs.clear();
            prefs.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}