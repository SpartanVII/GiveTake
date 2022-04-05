package com.example.givetake.ui.profile.listProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.givetake.R;
import com.example.givetake.model.Product;

import java.util.List;

public class ListAdapterProducts extends ArrayAdapter<Product> {

    private List<Product> dataSet;
    Context mContext;

    // View lookup cache


    public ListAdapterProducts(List<Product> data, Context context) {
        super(context, R.layout.item_product, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Product product = (Product) getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product,parent,false);
        }

        TextView productName = convertView.findViewById(R.id.productName);
        productName.setText(product.getTitle());

        TextView productDesc = convertView.findViewById(R.id.productDesc);
        productDesc.setText(product.getDescription());

        ImageView img = convertView.findViewById(R.id.img);
        //img.setImageDrawable(product.getImg());

        // Return the completed view to render on screen
        return convertView;
    }
}

