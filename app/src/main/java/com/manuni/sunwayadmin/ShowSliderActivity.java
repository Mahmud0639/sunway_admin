package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.ActivityShowSliderBinding;

import java.util.ArrayList;

public class ShowSliderActivity extends AppCompatActivity {
    ActivityShowSliderBinding binding;
    private ArrayList<SliderModel> list;
    private SliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityShowSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("SliderImages");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                binding.loadingCircle.setVisibility(View.VISIBLE);

                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        SliderModel data = dataSnapshot.getValue(SliderModel.class);
                        list.add(data);
                    }
                    binding.sliderRV.setLayoutManager(new LinearLayoutManager(ShowSliderActivity.this));
                    binding.sliderRV.setHasFixedSize(true);
                    adapter = new SliderAdapter(ShowSliderActivity.this,list);
                    binding.sliderRV.setAdapter(adapter);

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