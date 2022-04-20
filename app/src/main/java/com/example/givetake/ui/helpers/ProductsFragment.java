package com.example.givetake.ui.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.givetake.ui.activities.MyProductActivity;

import java.util.List;


public class ProductsFragment extends Fragment {
    ListAdapterProducts listAdapterInfo;
    private Presenter presenter;
    private ListView listView;

    public ProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        presenter = new Presenter();
        User user = presenter.getUser(key);
        listView = view.findViewById(R.id.listviewProduct);
        List<Product> productList = user.getTradeProducts();
        listAdapterInfo = new ListAdapterProducts(productList, getContext());
        listView.setAdapter(listAdapterInfo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MyProductActivity.class);
                intent.putExtra("productKey", productList.get(position).getId());
                startActivity(intent);
            }
        });

        TextView textVoidProducts = view.findViewById(R.id.textVoidProducts);
        if (productList.size()==0) textVoidProducts.setVisibility(View.VISIBLE);
        else textVoidProducts.setVisibility(View.INVISIBLE);

        return view;
    }

}