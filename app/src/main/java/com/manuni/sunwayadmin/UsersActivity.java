package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.ActivityUsersBinding;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    ActivityUsersBinding binding;
    private DatabaseReference reference;
    private ArrayList<Users> list;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                binding.progressBar.setVisibility(View.VISIBLE);
                if (snapshot.exists()){

                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Users data = dataSnapshot.getValue(Users.class);
                        list.add(data);

                    }
                    adapter = new UsersAdapter(UsersActivity.this,list);
                    binding.usersRV.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
                    binding.usersRV.setAdapter(adapter);
                    binding.usersRV.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();

                    binding.progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });





    }
}