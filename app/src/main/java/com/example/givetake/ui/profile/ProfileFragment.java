package com.example.givetake.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentProfileBinding;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.EditProfileActivity;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    Presenter presenter = new Presenter();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        final TextView name = binding.name;
        final TextView address = binding.address;
        final Button editProfile = binding.profile;

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        User user = presenter.getUser(key);
        name.setText(user.getName());
        address.setText(user.getAddressToString());
        //address.setText(user.getAddress().toString());

        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}