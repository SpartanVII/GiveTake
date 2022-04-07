package com.example.givetake.ui.profile.listProduct;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.givetake.R;
import com.example.givetake.presenter.Presenter;

public class ProfileProductFragment extends Fragment {
    private Presenter presenter = new Presenter();
    public ProfileProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);


        SharedPreferences prefs = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String key = prefs.getString("email", null).split("@")[0];


        ListView listView = view.findViewById(R.id.listview);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(new ListAdapterProducts(presenter.getUser(key).getTradeProducts(), getContext()));

        return view;
    }
}