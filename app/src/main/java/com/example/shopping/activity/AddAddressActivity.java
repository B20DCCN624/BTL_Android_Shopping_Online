package com.example.shopping.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopping.R;
import com.example.shopping.domain.Address;
import com.example.shopping.helper.SQLiteHelper;

public class AddAddressActivity extends AppCompatActivity {
    private EditText edtAddAddress;
    private Button btnAdd, btnCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        edtAddAddress = findViewById(R.id.edt_add_address);
        btnAdd = findViewById(R.id.btn_add);
        btnCancle = findViewById(R.id.btn_cancle);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String add =edtAddAddress.getText().toString();
                if(!add.isEmpty()) {
                    Address i = new Address(add);
                    SQLiteHelper db = new SQLiteHelper(getApplicationContext());
                    db.addAddress(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}