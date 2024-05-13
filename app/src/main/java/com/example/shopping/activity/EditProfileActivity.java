package com.example.shopping.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shopping.R;
import com.example.shopping.domain.Profile;
import com.example.shopping.helper.SQLiteHelper;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView arrowBack, imageUser;
    private EditText edtUsername, edtEmail, edtBirthDay, edtContact;
    private Button btnUpdateProfile;
    private SQLiteHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = new SQLiteHelper(getApplicationContext());
        arrowBack = findViewById(R.id.icon_arrow_back);
        imageUser = findViewById(R.id.edit_img_user);
        edtUsername = findViewById(R.id.edtProfileUsername);
        edtEmail = findViewById(R.id.edtProfileEmail);
        edtBirthDay = findViewById(R.id.edtBirthday);
        edtContact = findViewById(R.id.edtContact);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");

        edtUsername.setText(username);
        edtEmail.setText(email);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int yyyy =c.get(Calendar.YEAR);
                int mm = c.get(Calendar.MONTH);
                int dd = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                        edtBirthDay.setText(d + "/" + (m + 1) + "/" + y);
                    }
                }, yyyy, mm, dd);
                datePickerDialog.show();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String email = edtEmail.getText().toString();
                String date = edtBirthDay.getText().toString();
                String contact = edtContact.getText().toString();

                Profile profile = new Profile(username, email, date, contact);
                db.insertProfile(profile);

                finish();
            }
        });
    }
}