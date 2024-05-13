package com.example.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    private ArrayList<String> items;
    private Context context;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    public SizeAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_size, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sizeText.setText(items.get(position));
        holder.sizeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if(selectedPosition == holder.getAdapterPosition()) {
            holder.sizeLayout.setBackgroundResource(R.drawable.size_selected);
            holder.sizeText.setTextColor(context.getResources().getColor(R.color.pinkRed));
        } else {
            holder.sizeLayout.setBackgroundResource(R.drawable.size_unselected);
            holder.sizeText.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sizeText;
        ConstraintLayout sizeLayout;
        public ViewHolder(@NonNull View view) {
            super(view);
            sizeText = view.findViewById(R.id.sizeText);
            sizeLayout = view.findViewById(R.id.sizeLayout);
        }
    }
}
