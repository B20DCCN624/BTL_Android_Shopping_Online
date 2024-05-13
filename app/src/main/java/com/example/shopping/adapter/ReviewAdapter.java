package com.example.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.shopping.R;
import com.example.shopping.domain.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> items;
    private Context context;
    public ReviewAdapter(Context context, ArrayList<Review> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTxt.setText(items.get(position).getName());
        holder.reviewRatingTxt.setText(""+items.get(position).getRating());
        holder.descTxt.setText(items.get(position).getDescription());

        String imageUrl = items.get(position).getPicUrl();
        Glide.with(context)
                .load(imageUrl)
                .transform(new GranularRoundedCorners(100,100,100,100))
                .into(holder.imgPic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPic;
        private TextView nameTxt, reviewRatingTxt, descTxt;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgPic = view.findViewById(R.id.pic);
            nameTxt = view.findViewById(R.id.nameTxt);
            reviewRatingTxt = view.findViewById(R.id.review_ratingTxt);
            descTxt = view.findViewById(R.id.descTxt);
        }
    }
}
