package com.example.givetake.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.Review;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.activities.MyProductActivity;
import com.example.givetake.ui.helpers.ProductAdapter;
import com.example.givetake.ui.helpers.ReviewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ReviewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", "");

        presenter = new Presenter();
        User user = presenter.getUser(key);
        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle!=null){
            String vendorKey = bundle.getString("vendorKey", null);
            if (vendorKey != null) user = presenter.getUser(vendorKey);
        }

        recyclerView = view.findViewById(R.id.recyclerviewReview);
        List<Review> reviewList = new ArrayList<>();
        if (user != null){
            reviewList.addAll(user.getReviewsForMe());
            reviewList.sort((a,b) ->  b.getReviewDate().compareTo(a.getReviewDate()));
        }
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(reviewAdapter);

        TextView textVoidReviews = view.findViewById(R.id.textVoidReviews);
        if (reviewList.isEmpty()) textVoidReviews.setVisibility(View.VISIBLE);

        return view;
    }
}