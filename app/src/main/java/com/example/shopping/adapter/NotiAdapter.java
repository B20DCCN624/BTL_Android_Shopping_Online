package com.example.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.domain.Payment;

import java.util.ArrayList;
import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder> {
    private List<Payment> payment;
    private Context context;
    public NotiAdapter(Context context) {
        this.context = context;
        payment = new ArrayList<>();
    }
    public void setData(List<Payment> list) {
        this.payment = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_noti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.priceTxt.setText("Tổng tiền: $" + payment.get(position).getPriceTotal());
        holder.dateTxt.setText(payment.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return payment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView priceTxt, dateTxt;
        public ViewHolder(@NonNull View view) {
            super(view);

            dateTxt = view.findViewById(R.id.tvDate);
            priceTxt = view.findViewById(R.id.notiPriceTxt);
        }
    }
}
