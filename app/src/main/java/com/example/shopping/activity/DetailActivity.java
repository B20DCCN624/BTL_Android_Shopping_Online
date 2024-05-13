package com.example.shopping.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopping.R;
import com.example.shopping.adapter.SizeAdapter;
import com.example.shopping.adapter.SliderAdapter;
import com.example.shopping.adapter.WishlistAdapter;
import com.example.shopping.domain.Items;
import com.example.shopping.domain.SliderItems;
import com.example.shopping.fragment.DescriptionsFragment;
import com.example.shopping.fragment.ReviewFragment;
import com.example.shopping.helper.ManagmentCart;
import com.example.shopping.helper.SQLiteHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private SQLiteHelper db;
    private ImageView btnBack, btnShare, btnSave;
    private ViewPager viewPagerDetail;
    private TabLayout tabLayoutDetail;
    private ViewPager2 viewPagerSlider;
    private TextView titleTxt, priceTxt, ratingTxt;
    private RatingBar ratingBar;
    private RecyclerView rcvSize;
    private Button btnAddtoCart;

    private Items object;
    private WishlistAdapter wishlistAdapter;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private Handler sliderHandle = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new SQLiteHelper(getApplicationContext());
        titleTxt = findViewById(R.id.tv_title);
        priceTxt = findViewById(R.id.tv_price);
        ratingBar = findViewById(R.id.detail_ratingBar);
        ratingTxt = findViewById(R.id.ratingTxt);
        btnAddtoCart = findViewById(R.id.btnAddtoCart);
        btnBack = findViewById(R.id.icon_back);
        btnShare = findViewById(R.id.icon_share);
        btnSave = findViewById(R.id.icon_save);
        viewPagerSlider = findViewById(R.id.detail_viewPagerSlider);
        rcvSize = findViewById(R.id.rcvSize);
        viewPagerDetail = findViewById(R.id.detail_viewPager);
        tabLayoutDetail = findViewById(R.id.detail_tabLayout);

        managmentCart = new ManagmentCart(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleTxt.getText().toString();
                String priceString = priceTxt.getText().toString();
                priceString = priceString.replace("$", "");
                double price = Double.parseDouble(priceString);
                String ratingString = ratingTxt.getText().toString();
                ratingString = ratingString.replaceAll("[^0-9.]", "");
                float rating = Float.parseFloat(ratingString);
                String picUrl = object.getPicUrl().get(0);

                ArrayList<String> picUrls = new ArrayList<>();
                picUrls.add(picUrl);

                Items item = new Items(title, picUrls, price, rating);
                long result = db.insertItem(item);
                if (result == -1) {
                    Toast.makeText(getApplicationContext(), "Sản phẩm đã tồn tại trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getBundles();
        initBanners();
        initSize();
        setupViewPager();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đảm bảo đóng cơ sở dữ liệu khi hoạt động bị hủy
        if (db != null) {
            db.close();
        }
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        rcvSize.setAdapter(new SizeAdapter(this, list));
        rcvSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for(int i=0; i<object.getPicUrl().size(); i++) {
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        viewPagerSlider.setAdapter(new SliderAdapter(getApplicationContext(), sliderItems, viewPagerSlider));
        viewPagerSlider.setClipToPadding(false);
        viewPagerSlider.setClipChildren(false);
        viewPagerSlider.setOffscreenPageLimit(3);
        viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object = (Items) getIntent().getSerializableExtra("object");
        titleTxt.setText(object.getTitle());
        priceTxt.setText("$"+object.getPrice());
        ratingBar.setRating((float) object.getRating());
        ratingTxt.setText(object.getRating()+" Rating");

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                object.setNumberinCart(numberOrder);
                managmentCart.insertItem(object);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionsFragment tab1 = new DescriptionsFragment();
        ReviewFragment tab2 = new ReviewFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);

        adapter.addFrag(tab1, "Descriptions");
        adapter.addFrag(tab2, "Reviews");

        viewPagerDetail.setAdapter(adapter);
        tabLayoutDetail.setupWithViewPager(viewPagerDetail);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}