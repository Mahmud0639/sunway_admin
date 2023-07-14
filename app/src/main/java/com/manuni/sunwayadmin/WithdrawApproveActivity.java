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
import com.manuni.sunwayadmin.databinding.ActivityWithdrawApproveBinding;

import java.util.ArrayList;

public class WithdrawApproveActivity extends AppCompatActivity {
    ActivityWithdrawApproveBinding binding;
    private ArrayList<WithdrawModel> list;
    private WithdrawAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawApproveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loadingCircle.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Withdraws");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        WithdrawModel data = dataSnapshot.getValue(WithdrawModel.class);
                        list.add(0,data);
                    }

                    binding.withdrawApproveRV.setLayoutManager(new LinearLayoutManager(WithdrawApproveActivity.this));
                    binding.withdrawApproveRV.setHasFixedSize(true);
                    adapter = new WithdrawAdapter(list,WithdrawApproveActivity.this);
                    binding.withdrawApproveRV.setAdapter(adapter);
                    binding.loadingCircle.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                binding.loadingCircle.setVisibility(View.INVISIBLE);
            }
        });

    }
}