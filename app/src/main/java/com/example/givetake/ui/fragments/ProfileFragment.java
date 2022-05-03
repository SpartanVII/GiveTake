package com.example.givetake.ui.fragments;

import android.app.AlertDialog;
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
import com.example.givetake.model.Review;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.CompleteReviewActivity;
import com.example.givetake.ui.activities.EditProfileActivity;
import com.example.givetake.ui.helpers.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Presenter presenter;
    private Bundle bundle;
    private User user;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView name = binding.name;
        final TextView address = binding.address;
        final Button editProfile = binding.profile;
        final ImageView profileImg = binding.prfileImg;
        final TextView reputation = binding.reputacion;
        final TabLayout tabLayout = binding.tab;
        final ViewPager2 viewPager = binding.viewPager;
        presenter = new Presenter();

        SharedPreferences prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        user = presenter.getUser(key);
        bundle = requireActivity().getIntent().getExtras();
        if (bundle!=null){
            String vendorKey = bundle.getString("vendorKey", null);
            if (vendorKey!=null) {
                user = presenter.getUser(vendorKey);
                editProfile.setVisibility(View.GONE);
            }
        }
        if (user != null) {
            name.setText(user.getName());
            address.setText(user.obtainAddressLine());
            reputation.setText("Reputación " + user.getGlobalReputation());
            //profileImg.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_logged));
        }
        else {
            name.setText(R.string.textview_conection_error);
            address.setText("");
            reputation.setText("Reputación ");
            editProfile.setVisibility(View.GONE);
        }
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        viewPager.setAdapter(new TabAdapter(requireActivity()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Productos");
            else tab.setText("Opiniones");
        }).attach();

        pendentReview();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void pendentReview(){
        SharedPreferences prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        int init = prefs.getInt("init",0);
        if (bundle==null && init == 0){
            List<Review> reviewList = new ArrayList<>();
            if(user != null) reviewList.addAll(user.getUncompletedReviews());
            if (!reviewList.isEmpty()){
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putInt("init", 1).apply();

                Review review = reviewList.get(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle(R.string.alert_dialog_complete_review);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.alert_dialog_complete_review_positive, (dialog, which) -> {
                    Intent intent = new Intent(requireContext(), CompleteReviewActivity.class);
                    intent.putExtra("review", review);
                    startActivity(intent);
                });
                if (review.getDaysShowedToComplete()==1){
                    builder.setMessage(R.string.alert_dialog_last_try_to_complete_review);
                    builder.setNegativeButton(R.string.alert_dialog_complete_review_negative_last, (dialog, which) -> {
                        presenter.decrementDaysToCompleteReview(review, user.getMail());
                    });
                }
                else {
                    builder.setMessage(R.string.alert_dialog_not_last_try_to_complete_review);
                    builder.setNegativeButton(R.string.alert_dialog_complete_review_negative_not_last, (dialog, which) -> {
                        presenter.decrementDaysToCompleteReview(review, user.getMail());
                    });
                }
                builder.show();
            }
        }
    }
}