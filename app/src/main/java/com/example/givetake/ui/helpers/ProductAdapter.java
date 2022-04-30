package com.example.givetake.ui.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.givetake.R;
import com.example.givetake.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements AdapterView.OnClickListener {
    private List<Product> items;
    private AdapterView.OnClickListener listener;
    Context mContext;

    public void setItems(List<Product> items) {
        this.items = items;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImg;
        private final TextView productName;
        private final TextView productDesc;

        public ProductViewHolder(View v) {
            super(v);
            productImg = v.findViewById(R.id.img);
            productName = v.findViewById(R.id.productName);
            productDesc = v.findViewById(R.id.productDesc);
        }
    }

    public ProductAdapter(List<Product> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product, viewGroup, false);
        mContext = v.getContext();
        v.setOnClickListener(this);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int i) {
        Glide.with(mContext).load(items.get(i).getImg()).centerCrop().into(viewHolder.productImg);
        viewHolder.productName.setText(items.get(i).getTitle());
        viewHolder.productDesc.setText(items.get(i).getDescription());
    }
}
