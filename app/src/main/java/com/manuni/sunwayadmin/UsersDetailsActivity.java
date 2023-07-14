package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.ActivityUsersDetailsBinding;

import java.util.ArrayList;

public class UsersDetailsActivity extends AppCompatActivity {
    ActivityUsersDetailsBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ArrayList<PackageModelAdmin> list;
    private PackageAdapterAdmin adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //auth = FirebaseAuth.getInstance();


        userId = getIntent().getStringExtra("userId");

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        list = new ArrayList<>();

        reference.child(userId).child("userPackInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        PackageModelAdmin data = null;
                        try {
                            data = dataSnapshot.getValue(PackageModelAdmin.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        list.add(0,data);
                    }
                    adapter = new PackageAdapterAdmin(UsersDetailsActivity.this,list);
                    binding.packRV.setLayoutManager(new LinearLayoutManager(UsersDetailsActivity.this));
                    binding.packRV.setAdapter(adapter);
                    binding.packRV.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding.packRV.setAdapter(null);

        adapter = null;

    }
}