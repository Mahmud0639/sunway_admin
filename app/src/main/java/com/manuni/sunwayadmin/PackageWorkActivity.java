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
import com.manuni.sunwayadmin.databinding.ActivityPackageWorkBinding;

import java.util.ArrayList;

public class PackageWorkActivity extends AppCompatActivity {
    ActivityPackageWorkBinding binding;
    private DatabaseReference ref;
    private ArrayList<PackModel> list;
    private PackAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackageWorkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.progressBar.setVisibility(View.VISIBLE);


        list = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("PackageInfo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PackModel data = dataSnapshot.getValue(PackModel.class);
                    list.add(data);
                }
                adapter = new PackAdapter(PackageWorkActivity.this,list);
                binding.allPackage.setAdapter(adapter);
                binding.allPackage.setLayoutManager(new LinearLayoutManager(PackageWorkActivity.this));
                adapter.notifyDataSetChanged();
                binding.allPackage.setHasFixedSize(true);
                binding.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}