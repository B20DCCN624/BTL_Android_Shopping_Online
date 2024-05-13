package com.example.shopping.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopping.BasicActivityInterface;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity implements BasicActivityInterface {
    protected FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    public FirebaseDatabase getFirebaseDatabase() {
        return database;
    }
}