package com.example.shopping.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.shopping.R;
import com.example.shopping.domain.Items;
import com.example.shopping.helper.SQLiteHelper;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{
    private ArrayList<Items> items;
    private Items currentItem;
    private SQLiteHelper db;
    private Context context;
    public WishlistAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }
    public void setData(ArrayList<Items> list) {
        this.items = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_wishlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.ratingBar.setRating(items.get(position).getRating());
        holder.priceTxt.setText("$"+items.get(position).getPrice());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());
        // Lấy URL từ Items
        ArrayList<String> picUrls = items.get(position).getPicUrl();
        if (picUrls != null && !picUrls.isEmpty()) {
            String imageUrl = picUrls.get(0);
            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(holder.wishlistPic);
        }

        db = new SQLiteHelper(context.getApplicationContext());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView wishlistPic;
        private TextView titleTxt, priceTxt, removeTxt;
        private RatingBar ratingBar;
        public ViewHolder(@NonNull View view) {
            super(view);

            wishlistPic = view.findViewById(R.id.wishlist_pic);
            titleTxt = view.findViewById(R.id.titleWishlistTxt);
            priceTxt = view.findViewById(R.id.priceWishlist);
            ratingBar = view.findViewById(R.id.ratingBarWishlist);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Thông báo xóa!");
                        builder.setTitle("Bạn có chắc muốn xóa "+ items.get(position).getTitle()+" không?");
                        builder.setIcon(R.drawable.icon_delete);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteItem(items.get(position).getId());
                                items.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border_radius);
                        dialog.show();
                    }
                    return true;
                }
            });
        }
    }
}
