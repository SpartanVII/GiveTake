package com.example.givetake.ui.helpers;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
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
import com.example.givetake.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> implements AdapterView.OnClickListener {
    private List<Review> items;
    private AdapterView.OnClickListener listener;
    Context mContext;

    public void setItems(List<Review> items) {
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


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImg;
        private final TextView productName;
        private final TextView reviewComentary;
        private final TextView reviewExperience;
        private final TextView reviewDate;


        public ReviewViewHolder(View v) {
            super(v);
            productImg = v.findViewById(R.id.img);
            productName = v.findViewById(R.id.productName);
            reviewComentary = v.findViewById(R.id.reviewComentary);
            reviewExperience = v.findViewById(R.id.reviewExperience);
            reviewDate = v.findViewById(R.id.reviewDate);

        }
    }

    public ReviewAdapter(List<Review> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_review, viewGroup, false);
        mContext = v.getContext();
        v.setOnClickListener(this);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, int i) {
        Glide.with(mContext).load(items.get(i).getProductImg()).centerCrop().into(viewHolder.productImg);
        viewHolder.productName.setText(items.get(i).getProductName());
        if (items.get(i).getComentary()== null || items.get(i).getComentary().isEmpty()) viewHolder.reviewComentary.setVisibility(View.GONE);
        else viewHolder.reviewComentary.setText(items.get(i).getComentary());
        viewHolder.reviewExperience.setText(mContext.getString(R.string.item_review_experience,""+items.get(i).getExperienceFromScore()));
        String texto = viewHolder.reviewDate.getText().toString();
        texto = texto.replace("shortName", "<font color=" + mContext.getColor(R.color.black) + ">"+items.get(i).getShortName()+"</font>");
        texto = texto.replace("dateWithLongMonth",items.get(i).getFormatReviewDate());
        viewHolder.reviewDate.setText(Html.fromHtml(texto, 0));

    }
}
