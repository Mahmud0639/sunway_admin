package com.manuni.sunwayadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.ActivityShowProductsBinding;

import java.util.ArrayList;

public class ShowProductsActivity extends AppCompatActivity {
    ActivityShowProductsBinding binding;
    private DatabaseReference databaseReference;
    private ProductsAdapter adapter;
    private ArrayList<ProductsModel> list;

    private ProgressDialog dialog;

    private String pacId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pacId = getIntent().getStringExtra("packId");


        dialog = new ProgressDialog(ShowProductsActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();



        databaseReference = FirebaseDatabase.getInstance().getReference();

        list = new ArrayList<>();

        databaseReference.child("AllProductsPriceInfo").child(pacId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                       ProductsModel data = dataSnapshot.getValue(ProductsModel.class);

                       list.add(data);
                    }

                    adapter = new ProductsAdapter(ShowProductsActivity.this,list,pacId);
                    binding.productRV.setAdapter(adapter);
                    binding.productRV.setLayoutManager(new LinearLayoutManager(ShowProductsActivity.this));
                    binding.productRV.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


    }
}