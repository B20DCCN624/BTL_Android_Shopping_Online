package com.example.shopping.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.adapter.AddressAdapter;
import com.example.shopping.domain.Address;
import com.example.shopping.helper.SQLiteHelper;

import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.ItemListener {
    private AddressAdapter addressAdapter;
    private SQLiteHelper db;
    private RecyclerView rcvAdd;
    private Button btnAddAddress;
    private ImageView arrowBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        db = new SQLiteHelper(getApplicationContext());
        rcvAdd = findViewById(R.id.rcv_address);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        arrowBack = findViewById(R.id.icon_arrow_back_address);

        addressAdapter = new AddressAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rcvAdd.setLayoutManager(linearLayoutManager);
        List<Address> list = db.getAll();

        addressAdapter.setData(list);
        rcvAdd.setAdapter(addressAdapter);
        addressAdapter.setItemListener(this);

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Address address = addressAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), DetailAddressActivity.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Address> add = db.getAll();
        addressAdapter.setData(add);
        addressAdapter.notifyDataSetChanged();
    }
}