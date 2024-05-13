package com.example.shopping.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopping.R;
import com.example.shopping.domain.Address;
import com.example.shopping.helper.SQLiteHelper;

public class DetailAddressActivity extends AppCompatActivity {
    private EditText detailAddress;
    private Button btnUpdate, btnDelete;
    private SQLiteHelper db;
    private Address currentAddress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_address);

        Intent intent = getIntent();
        db = new SQLiteHelper(getApplicationContext());
        detailAddress = findViewById(R.id.edt_detail_address);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        currentAddress = (Address) intent.getSerializableExtra("address");
        setData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = detailAddress.getText().toString();
                if(!address.isEmpty()) {
                    Address i = new Address(currentAddress.getId(), address);
                    db.updateAddress(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Thông báo xóa!");
                builder.setTitle("Bạn có chắc muốn xóa địa chỉ "+ currentAddress.getAdd()+" không?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteAddress(currentAddress.getId());
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setData() {
        if (currentAddress == null) {
            return;
        }
        detailAddress.setText(currentAddress.getAdd());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}