package com.example.shopping.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DescriptionsFragment extends Fragment {
    private FirebaseDatabase database;
    private TextView descTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_descriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descTxt = view.findViewById(R.id.descriptionTxt);
//        descTxt.setText(getArguments().getString("description", ""));
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("Items");
        // Lấy theo ID
        String itemId = getArguments().getString("itemId");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        // Lấy mô tả từ mục cụ thể
                        String description = itemSnapshot.child("description").getValue(String.class);
                        descTxt.setText(description);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        database = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef = database.getReference("Items");
//
//        // Get itemId from arguments
//        String itemId = getArguments().getString("itemId");
//        if (itemId != null) {
//            DatabaseReference itemRef = dbRef.child(itemId);
//            itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        // Get description directly from the specific item node
//                        String description = snapshot.child("description").getValue(String.class);
//                        if (description != null) {
//                            descTxt.setText(description);
//                        } else {
//                            descTxt.setText("No description available.");
//                        }
//                    } else {
//                        descTxt.setText("Item not found.");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    descTxt.setText("Error loading description. Error: " + error.getMessage());
//                }
//            });
//        } else {
//            descTxt.setText("Item ID is missing.");
//        }
    }
}
