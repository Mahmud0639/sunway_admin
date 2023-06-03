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
import com.manuni.sunwayadmin.databinding.ActivityAllPackagesBinding;

import java.util.ArrayList;

public class AllPackagesActivity extends AppCompatActivity {
    ActivityAllPackagesBinding binding;
    private ArrayList<PackModel> list;
    private PackAdapterForUpdate adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllPackagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadAllPackData();

        
    }
    private void loadAllPackData(){
        binding.loadingCircle.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("PackageInfo");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        PackModel data = dataSnapshot.getValue(PackModel.class);
                        list.add(data);
                    }
                    binding.packRV.setLayoutManager(new LinearLayoutManager(AllPackagesActivity.this));
                    binding.packRV.setHasFixedSize(true);
                    adapter = new PackAdapterForUpdate(AllPackagesActivity.this,list);
                    binding.packRV.setAdapter(adapter);
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