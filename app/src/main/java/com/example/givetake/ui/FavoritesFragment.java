package com.example.givetake.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.givetake.R;
import com.example.givetake.databinding.FragmentFavoritesBinding;
import com.example.givetake.model.Product;
import com.example.givetake.model.User;
import com.example.givetake.presenter.Presenter;
import com.example.givetake.ui.activities.InfoProductActivity;
import com.example.givetake.ui.profile.helpers.ListAdapterProducts;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    ListAdapterProducts listAdapterInfo;
    private Presenter presenter;
    private Context appContext;
    private ListView listView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        appContext = getContext();
        assert appContext != null;
        SharedPreferences prefs = appContext.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];

        presenter = new Presenter();
        User user = presenter.getUser(key);
        listView = view.findViewById(R.id.listviewFavs);
        List<String> keyList = user.getFavProducts();
        List<Product> productList = presenter.getProductListUsingKeys(keyList);
        listAdapterInfo = new ListAdapterProducts(productList, getContext());
        listView.setAdapter(listAdapterInfo);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(appContext, InfoProductActivity.class);
            intent.putExtra("productKey", productList.get(position).getId());
            startActivity(intent);
        });

        TextView textVoidFavs = view.findViewById(R.id.textVoidFavs);
        if (productList.size()==0) textVoidFavs.setVisibility(View.VISIBLE);
        else textVoidFavs.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}