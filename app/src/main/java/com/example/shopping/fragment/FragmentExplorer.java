package com.example.shopping.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopping.BasicActivityInterface;
import com.example.shopping.MainActivity;
import com.example.shopping.R;
import com.example.shopping.activity.NotificationsActivity;
import com.example.shopping.adapter.CategoryAdapter;
import com.example.shopping.adapter.ItemAdapter;
import com.example.shopping.adapter.SliderAdapter;
import com.example.shopping.domain.Category;
import com.example.shopping.domain.Items;
import com.example.shopping.domain.SliderItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentExplorer extends Fragment implements BasicActivityInterface {
    private FirebaseDatabase database;
    private EditText edtSearch;
    private ImageView notifications;
    private ProgressBar progressBar, progressBarOffical, progressBarPopular;
    private ViewPager2 viewPagerBanner;
    private RecyclerView rcvOffical, rcvPopular;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);

        // Lấy đối tượng FirebaseDatabase từ BaseActivityInterface
        BasicActivityInterface baseActivity = (BasicActivityInterface) getActivity();
        if (baseActivity != null) {
            database = baseActivity.getFirebaseDatabase();
        }

        progressBar = view.findViewById(R.id.progressBar);
        progressBarOffical = view.findViewById(R.id.progressBarOffical);
        progressBarPopular = view.findViewById(R.id.progressBarPopular);
        viewPagerBanner = view.findViewById(R.id.viewPagerSlider);
        rcvOffical = view.findViewById(R.id.rcvOffical);
        rcvPopular = view.findViewById(R.id.rcvPopular);
        edtSearch = view.findViewById(R.id.edtSearch);
        notifications = view.findViewById(R.id.iconNotifications);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    searchItems(s.toString());
                } else {
                    initItems();  // Nạp lại tất cả các mục nếu không có văn bản tìm kiếm
                }
            }
        });

        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    // Gọi phương thức toggleBottomNav, truyền 'true' để hiện hoặc 'false' để ẩn
                    mainActivity.toggleBottomNav(!hasFocus);
                }
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                startActivity(intent);
            }
        });

        initBanner();
        initCategory();
        initItems();

        return view;
    }

    private void initBanner() {
        DatabaseReference dbRef = database.getReference("Banner");
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                }
                banners(items);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initCategory() {
        DatabaseReference dbRef = database.getReference("Category");
        progressBarOffical.setVisibility(View.VISIBLE);
        ArrayList<Category> items = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Category.class));
                    }
                    if(!items.isEmpty()) {
                        rcvOffical.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        rcvOffical.setAdapter(new CategoryAdapter(getContext(), items));
                    }
                    progressBarOffical.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initItems() {
        DatabaseReference dbRef = database.getReference("Items");
        progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<Items> items = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(Items.class));
                    }
                    if(!items.isEmpty()) {
                        rcvPopular.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        rcvPopular.setAdapter(new ItemAdapter(getContext(), items));
                    }
                    progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchItems(String query) {
        DatabaseReference dbRef = database.getReference("Items");
        progressBarPopular.setVisibility(View.VISIBLE);
        final ArrayList<Items> items = new ArrayList<>();
        final String[] keywords = query.toLowerCase().split("\\s+");

        Query searchQuery = dbRef.orderByChild("title");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot issue : snapshot.getChildren()) {
                    Items item = issue.getValue(Items.class);
                    if (item != null) {
                        String title = item.getTitle().toLowerCase();
                        boolean containsAllKeywords = true;
                        for (String keyword : keywords) {
                            if (!title.contains(keyword)) {
                                containsAllKeywords = false;
                                break;
                            }
                        }
                        if (containsAllKeywords) {
                            items.add(item);
                        }
                    }
                }
                if (!items.isEmpty()) {
                    rcvPopular.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    rcvPopular.setAdapter(new ItemAdapter(getContext(), items));
                } else {
                    // Hiển thị rcv rỗng nếu không tìm thấy kết quả
                    rcvPopular.setAdapter(null);
                }
                progressBarPopular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBarPopular.setVisibility(View.GONE);
            }
        });
    }


    private void banners(ArrayList<SliderItems> items) {
        viewPagerBanner.setAdapter(new SliderAdapter(getContext(), items, viewPagerBanner));
        viewPagerBanner.setClipToPadding(false);
        viewPagerBanner.setClipChildren(false);
        viewPagerBanner.setOffscreenPageLimit(3);
        viewPagerBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        viewPagerBanner.setPageTransformer(compositePageTransformer);
    }

    @Override
    public FirebaseDatabase getFirebaseDatabase() {
        return database;
    }
}
