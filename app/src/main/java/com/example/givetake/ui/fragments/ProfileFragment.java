package com.example.givetake.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentProfileBinding;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.EditProfileActivity;
import com.example.givetake.ui.helpers.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Presenter presenter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView name = binding.name;
        final TextView address = binding.address;
        final Button editProfile = binding.profile;
        final ImageView profileImg = binding.prfileImg;
        final TextView reputacion = binding.reputacion;
        final TabLayout tabLayout = binding.tab;
        final ViewPager2 viewPager = binding.viewPager;
        presenter = new Presenter();

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        User user = presenter.getUser(key);
        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle!=null){
            String vendorKey = bundle.getString("vendorKey", null);
            user = presenter.getUser(vendorKey);
            editProfile.setVisibility(View.GONE);
        }

        name.setText(user.getName());
        address.setText(user.obtainAddressLine());
        reputacion.setText("Reputación "+ user.getGlobalScoreToString());
        //profileImg.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_logged));

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        viewPager.setAdapter(new TabAdapter(requireActivity()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Productos");
            else tab.setText("Opiniones");
        }).attach();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}