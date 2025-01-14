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
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.activities.MyProductActivity;
import com.example.givetake.ui.helpers.ProductAdapter;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", "");

        presenter = new Presenter();
        User user = presenter.getUser(key);
        Bundle bundle = requireActivity().getIntent().getExtras();
        if (bundle!=null){
            String vendorKey = bundle.getString("vendorKey", null);
            if (vendorKey != null) user = presenter.getUser(vendorKey);
        }

        recyclerView = view.findViewById(R.id.recyclerviewProduct);
        List<Product> productList = new ArrayList<>();
        if (user != null) productList.addAll(user.getTradeProducts());
        ProductAdapter productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);

        productAdapter.setOnClickListener(v -> {
            Intent intent;
            if (bundle!= null){
                intent = new Intent(getContext(), InfoProductActivity.class);
                intent.putExtra("parent","");
            }
            else intent = new Intent(getContext(), MyProductActivity.class);
            intent.putExtra("productKey", productList.get(recyclerView.getChildAdapterPosition(v)).getId());
            startActivity(intent);
        });

        TextView textVoidProducts = view.findViewById(R.id.textVoidProducts);
        if (productList.isEmpty()) textVoidProducts.setVisibility(View.VISIBLE);
        else textVoidProducts.setVisibility(View.INVISIBLE);

        return view;
    }
}