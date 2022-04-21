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

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder>
                                implements AdapterView.OnClickListener {
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


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImg;
        private final TextView productName;
        private final TextView productDesc;

        public CardViewHolder(View v) {
            super(v);
            productImg = v.findViewById(R.id.img);
            productName = v.findViewById(R.id.productName);
            productDesc = v.findViewById(R.id.productDesc);
        }
    }

    public CardViewAdapter(List<Product> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_product, viewGroup, false);
        mContext = v.getContext();
        v.setOnClickListener(this);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int i) {
        Glide.with(mContext).load(items.get(i).getImg()).centerCrop()
                .apply(RequestOptions.circleCropTransform()).into(viewHolder.productImg);
        viewHolder.productName.setText(items.get(i).getTitle());
        viewHolder.productDesc.setText(items.get(i).getDescription());
    }
}
