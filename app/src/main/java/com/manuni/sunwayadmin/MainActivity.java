package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private boolean taskStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UsersActivity.class));
            }
        });

        binding.packageUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PackageUpload.class));
            }
        });

        binding.packWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PackageWorkActivity.class));
            }
        });

        binding.uploadSliderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddSliderImageActivity.class));
            }
        });

        binding.allUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Users");
                d.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String id = ""+dataSnapshot.child("uid").getValue();

                            Toast.makeText(MainActivity.this, ""+id, Toast.LENGTH_SHORT).show();

                            d.child(id).child("userPackInfo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                            String packKeyMy = ""+dataSnapshot1.child("packKey").getValue();

                                            Toast.makeText(MainActivity.this, ""+packKeyMy, Toast.LENGTH_SHORT).show();

                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("taskTaken","false");

                                            d.child(id).child("userPackInfo").child(packKeyMy).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(MainActivity.this, "TaskTaken updated.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }


                                    }
                                    System.exit(0);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });



    }

}