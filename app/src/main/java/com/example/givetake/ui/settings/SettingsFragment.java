package com.example.givetake.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentSettingsBinding;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.LoginActivity;
import com.example.givetake.ui.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private FirebaseAuth mAuth;
    private Presenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];
        mAuth = FirebaseAuth.getInstance();
        presenter = new Presenter();

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Confirmación").setMessage("¿Estas seguro de eliminar la cuenta?")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()){
                                            showAlert();
                                        }
                                        else{
                                            presenter.deleteUser(key);
                                            showHome();
                                        }
                                    }
                                });
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showHome(){
        Intent homeIntent = new Intent(getContext(), MainActivity.class);
        SharedPreferences.Editor prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit();
        prefs.clear();
        prefs.apply();
        homeIntent.putExtra("isRegistered","false");
        startActivity(homeIntent);
    }

    public void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Error").setMessage("Necesitas volver a iniciar sesión para poder realizar la acción")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(getContext(), LoginActivity.class);
                        startActivity(homeIntent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}