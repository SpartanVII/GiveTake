package com.example.givetake.ui.profile;

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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentProfileBinding;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.EditProfileActivity;
import com.example.givetake.ui.profile.listProduct.ProductsFragment;
import com.example.givetake.ui.profile.listProduct.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Presenter presenter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView name = binding.name;
        final TextView address = binding.address;
        final Button editProfile = binding.profile;
        final ImageView profileImg = binding.prfileImg;
        presenter = new Presenter();


        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        User user = presenter.getUser(key);
        name.setText(user.getName());
        address.setText(user.obtainAddressLine());
        //profileImg.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_logged));

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        //TabLayer
        tabLayout = binding.tab;
        viewPager = binding.viewPager;

        tabLayout.setupWithViewPager(viewPager);
        TabAdapter tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(new ProductsFragment(),"Productos");
        //tabAdapter.addFragment(null,"Opiniones");
        viewPager.setAdapter(tabAdapter);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}