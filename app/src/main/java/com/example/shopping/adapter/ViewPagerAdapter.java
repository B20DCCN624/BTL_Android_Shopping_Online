package com.example.shopping.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.shopping.fragment.FragmentCart;
import com.example.shopping.fragment.FragmentExplorer;
import com.example.shopping.fragment.FragmentProfile;
import com.example.shopping.fragment.FragmentWishlist;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int pageNum;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.pageNum=behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new FragmentExplorer();
            case 1: return new FragmentWishlist();
            case 2: return new FragmentCart();
            case 3: return new FragmentProfile();
            default:return new FragmentExplorer();
        }
    }
    @Override
    public int getCount() {
        return pageNum;
    }
}
