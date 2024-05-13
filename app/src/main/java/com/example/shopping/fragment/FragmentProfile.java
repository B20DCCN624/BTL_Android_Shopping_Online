package com.example.shopping.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopping.R;
import com.example.shopping.activity.AboutActivity;
import com.example.shopping.activity.AddressActivity;
import com.example.shopping.activity.EditProfileActivity;
import com.example.shopping.activity.HelpActivity;
import com.example.shopping.activity.LoginActivity;
import com.example.shopping.domain.Profile;
import com.example.shopping.helper.SQLiteHelper;

public class FragmentProfile extends Fragment {
    private RelativeLayout editProfile;
    private LinearLayout itemAddress, itemAbout, itemHelp;
    private TextView tvUsername, tvEmail, tvLogout;
    private SQLiteHelper db;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new SQLiteHelper(getContext());
        tvLogout = view.findViewById(R.id.tv_logout);
        editProfile = view.findViewById(R.id.editProfile);
        itemAddress = view.findViewById(R.id.item_address);
        itemAbout = view.findViewById(R.id.item_about);
        itemHelp = view.findViewById(R.id.item_help);
        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);

        updateProfileInfo();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                sendData();
            }
        });

        itemAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });
        itemHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });
        itemAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void sendData() {
        String username = tvUsername.getText().toString();
        String email = tvEmail.getText().toString();

        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileInfo();
    }

    private void updateProfileInfo() {
        Profile profile = db.getAllProfile();
        if (profile != null) {
            tvUsername.setText(profile.getUsername());
            tvEmail.setText(profile.getEmail());
        } else {
            tvUsername.setText("No username");
            tvEmail.setText("No email");
        }
    }
}
