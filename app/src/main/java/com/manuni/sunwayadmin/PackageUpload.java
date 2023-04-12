package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manuni.sunwayadmin.databinding.ActivityPackageUploadBinding;

import java.util.HashMap;

public class PackageUpload extends AppCompatActivity {
    ActivityPackageUploadBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackageUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        progressDialog = new ProgressDialog(PackageUpload.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);



        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        String level = binding.levelET.getText().toString().trim();
        String perOrder = binding.perOrderET.getText().toString().trim();

        if (level.isEmpty()){
            binding.levelET.setError("Field can't be empty.");
            return;
        }
        if (perOrder.isEmpty()){
            binding.perOrderET.setError("Field can't be empty.");
            return;
        }

        uploadToDatabase(level,perOrder);
    }

    private void uploadToDatabase(String myLevel,String myPerOrder) {
        binding.levelET.setText("");
        binding.perOrderET.setText("");
        progressDialog.show();
        DatabaseReference packDB = FirebaseDatabase.getInstance().getReference().child("PackageInfo");

        String key = packDB.push().getKey();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("levelName",""+myLevel);
        hashMap.put("perOrderQuantity",""+myPerOrder);
        hashMap.put("id",""+key);

        packDB.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(PackageUpload.this, "Data added successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PackageUpload.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}