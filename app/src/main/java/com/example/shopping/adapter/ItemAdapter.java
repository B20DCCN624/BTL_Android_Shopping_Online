package com.example.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopping.R;
import com.example.shopping.activity.DetailActivity;
import com.example.shopping.domain.Items;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Items> items;
    private Context context;

    public ItemAdapter(Context context, ArrayList<Items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.oldPrice.setText("$"+items.get(position).getOldPrice());
        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.price.setText("$"+items.get(position).getPrice());
        holder.quantityReview.setText(""+items.get(position).getReview());
        holder.ratingBar.setRating(items.get(position).getRating());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        // Lấy URL từ Items
        String imageUrl = items.get(position).getPicUrl().get(0);
        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(holder.imgItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private RatingBar ratingBar;
        private TextView title;
        private TextView oldPrice, price, quantityReview;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgItem = view.findViewById(R.id.img_item1);
            ratingBar = view.findViewById(R.id.ratingBar);
            title = view.findViewById(R.id.title_item);
            oldPrice = view.findViewById(R.id.oldPrice);
            price = view.findViewById(R.id.newPrice);
            quantityReview = view.findViewById(R.id.quantityReview);
        }
    }
}
