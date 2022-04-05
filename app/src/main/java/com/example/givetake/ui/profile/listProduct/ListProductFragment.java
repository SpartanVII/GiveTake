package com.example.givetake.ui.profile.listProduct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.givetake.R;

import java.util.ArrayList;

public class ListProductFragment extends Fragment {


    ListView list;

    public ListProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_item, container, false);

        list = (ListView) view.findViewById(R.id.theLayout);
        ArrayList stringList= new ArrayList();

        stringList.add("Item 1A");


        CustomAdapter adapter = new CustomAdapter(stringList,getActivity());
        list.setAdapter(adapter);

        return view;
    }
}