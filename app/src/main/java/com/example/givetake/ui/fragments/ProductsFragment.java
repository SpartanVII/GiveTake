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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.givetake.R;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.activities.MyProductActivity;
import com.example.givetake.ui.helpers.CardViewAdapter;
import com.example.givetake.ui.helpers.ListAdapterProducts;

import java.util.List;


public class ProductsFragment extends Fragment {
    private RecyclerView recyclerView;
    private Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        presenter = new Presenter();
        User user = presenter.getUser(key);

        recyclerView = view.findViewById(R.id.recyclerviewProduct);
        List<Product> productList = user.getTradeProducts();
        CardViewAdapter cardViewAdapter = new CardViewAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cardViewAdapter);

        cardViewAdapter.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyProductActivity.class);
            intent.putExtra("productKey", productList.get(recyclerView.getChildAdapterPosition(v)).getId());
            startActivity(intent);
        });

        TextView textVoidProducts = view.findViewById(R.id.textVoidProducts);
        if (productList.isEmpty()) textVoidProducts.setVisibility(View.VISIBLE);
        else textVoidProducts.setVisibility(View.INVISIBLE);

        return view;
    }
}