package com.example.givetake.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentHomeBinding;
import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.AddProductActivity;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.helpers.ProductAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private Boolean isRegistered;
    private Presenter presenter;
    private TextView noProducts;
    private Spinner spinner;
    private String email;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        SharedPreferences prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        email = prefs.getString("email", null);
        isRegistered = email != null;

        recyclerView = binding.listviewHome;
        noProducts = binding.textVoidProductsHome;
        spinner = binding.spinnerHome;
        presenter = new Presenter();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_home, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List<Product> productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), InfoProductActivity.class);
            intent.putExtra("productKey", productList.get(recyclerView.getChildAdapterPosition(v)).getId());
            startActivity(intent);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                refreshRecyclerView(productList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        recyclerView.setClickable(isRegistered);
        final FloatingActionButton floatingButton = binding.floatingButton;
        if (!isRegistered) floatingButton.setVisibility(View.INVISIBLE);
        floatingButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddProductActivity.class);
            startActivity(intent);
        });

        SwipeRefreshLayout refresh = root.findViewById(R.id.homeLayout);
        refresh.setOnRefreshListener(() -> {
            presenter = new Presenter();
            refreshRecyclerView(productList);
            refresh.setRefreshing(false);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshRecyclerView(List<Product> productList) {
        productList.clear();
        productList.addAll(presenter.getProductsByTag(spinner.getSelectedItem().toString()));
        if (email!=null && presenter.getUser(email)!= null)
            productList.removeAll((presenter.getUser(email).getTradeProducts()));
        Collections.shuffle(productList, new Random());
        productAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        if (productList.isEmpty()) noProducts.setVisibility(View.VISIBLE);
        else noProducts.setVisibility(View.INVISIBLE);
    }
}