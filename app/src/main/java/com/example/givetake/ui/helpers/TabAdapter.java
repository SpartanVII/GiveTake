package com.example.givetake.ui.helpers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.givetake.ui.fragments.ProductsFragment;
import com.example.givetake.ui.fragments.ReviewsFragment;

import java.util.ArrayList;

public class TabAdapter extends FragmentStateAdapter {


    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public TabAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) return new ProductsFragment();
        else return new ReviewsFragment();

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
