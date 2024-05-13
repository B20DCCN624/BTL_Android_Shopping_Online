package com.example.shopping.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.adapter.WishlistAdapter;
import com.example.shopping.domain.Items;
import com.example.shopping.helper.SQLiteHelper;

import java.util.ArrayList;

public class FragmentWishlist extends Fragment{

    private RecyclerView rcvWishList;
    private ImageView emptyWishlist;
    private WishlistAdapter wishlistAdapter;
    private SQLiteHelper db;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        db = new SQLiteHelper(getContext());
        rcvWishList = view.findViewById(R.id.rcvWishlist);
        emptyWishlist = view.findViewById(R.id.emptyWishlistTxt);

        updateUI();

        wishlistAdapter = new WishlistAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvWishList.setLayoutManager(linearLayoutManager);
        ArrayList<Items> list = db.getAllItem();

        wishlistAdapter.setData(list);
        rcvWishList.setAdapter(wishlistAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        ArrayList<Items> list = db.getAllItem();
        wishlistAdapter.setData(list);
        wishlistAdapter.notifyDataSetChanged();
    }
    private void updateUI() {
        ArrayList<Items> list = db.getAllItem();
        if(list.isEmpty()) {
            emptyWishlist.setVisibility(View.VISIBLE);
            rcvWishList.setVisibility(View.GONE);
        } else {
            emptyWishlist.setVisibility(View.GONE);
            rcvWishList.setVisibility(View.VISIBLE);
        }
    }
}
