package com.example.givetake.ui.home;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentHomeBinding;
import com.example.givetake.model.Product;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.AddProductActivity;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.profile.helpers.CardViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private Presenter presenter;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Boolean isRegistered;

    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if (email!=null) isRegistered = true;
        else isRegistered = false;

        RecyclerView recyclerView = binding.listviewHome;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        spinner = binding.spinnerHome;
        presenter = new Presenter();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.category_home, R.layout.support_simple_spinner_dropdown_item );
        List<Product> productList = new ArrayList<>();
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                productList.clear();
                productList.addAll(presenter.getProductsByTag(spinner.getSelectedItem().toString()));
                CardViewAdapter cardViewAdapter = new CardViewAdapter(productList);
                cardViewAdapter.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), InfoProductActivity.class);
                    intent.putExtra("productKey", productList.get(recyclerView.getChildAdapterPosition(v)).getId());
                    startActivity(intent);
                });
                recyclerView.setAdapter(cardViewAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        recyclerView.setClickable(isRegistered);
        final FloatingActionButton floatingButton = binding.floatingButton;
        if (!isRegistered) floatingButton.setVisibility(View.INVISIBLE);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddProductActivity.class);
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