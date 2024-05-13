package com.example.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.MainActivity;
import com.example.shopping.R;
import com.example.shopping.adapter.NotiAdapter;
import com.example.shopping.domain.Payment;
import com.example.shopping.helper.SQLiteHelper;

import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView rcvNotifications;
    private ImageView btnBack, emptyNotifications;
    private NotiAdapter notiAdapter;
    private SQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        db = new SQLiteHelper(getApplicationContext());
        emptyNotifications = findViewById(R.id.emptyNotiTxt);
        rcvNotifications = findViewById(R.id.rcvNotifications);
        btnBack = findViewById(R.id.btnBackNoti);

        updateUI();

        notiAdapter = new NotiAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvNotifications.setLayoutManager(linearLayoutManager);
        List<Payment> list = db.getAllNoti();

        notiAdapter.setData(list);
        rcvNotifications.setAdapter(notiAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateUI() {
        List<Payment> list = db.getAllNoti();
        if(list.isEmpty()) {
            emptyNotifications.setVisibility(View.VISIBLE);
            rcvNotifications.setVisibility(View.GONE);
        } else {
            emptyNotifications.setVisibility(View.GONE);
            rcvNotifications.setVisibility(View.VISIBLE);
        }
    }
}