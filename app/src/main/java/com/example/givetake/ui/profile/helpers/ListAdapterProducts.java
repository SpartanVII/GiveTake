package com.example.givetake.ui.profile.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.givetake.R;
import com.example.givetake.model.Product;

import java.util.List;

public class ListAdapterProducts extends ArrayAdapter<Product> {

    private List<Product> dataSet;
    private ImageView productImg;
    private Product product;
    Context mContext;

    public ListAdapterProducts(List<Product> data, Context context) {
        super(context, R.layout.item_product, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product,parent,false);
        }

        product = getItem(position);

        productImg = convertView.findViewById(R.id.img);
        Glide.with(getContext()).load(product.getImg()).centerCrop().into(productImg);

        TextView productName = convertView.findViewById(R.id.productName);
        productName.setText(product.getTitle());

        TextView productDesc = convertView.findViewById(R.id.productDesc);
        productDesc.setText(product.getDescription());

        return convertView;
    }

}

