package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.manuni.sunwayadmin.databinding.ActivityUpdatePackImageBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdatePackImage extends AppCompatActivity {
    ActivityUpdatePackImageBinding binding;
    private String idPack,imagePack,levelPack,perOrderPack,sellPrice;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePackImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idPack = getIntent().getStringExtra("packId");
        imagePack = getIntent().getStringExtra("imagePack");
        levelPack = getIntent().getStringExtra("packLevel");
        perOrderPack = getIntent().getStringExtra("perOrder");
        sellPrice = getIntent().getStringExtra("sellingPrice");

        progressDialog = new ProgressDialog(UpdatePackImage.this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
/*
        try {
            imageUri = Uri.parse(imagePack);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        binding.levelET.setText(levelPack);
        binding.perOrderET.setText(perOrderPack);
        binding.sellingET.setText(sellPrice);

        try {
            Picasso.get().load(imagePack).placeholder(R.drawable.impl1).into(binding.imagePack);
        } catch (Exception e) {
            binding.imagePack.setImageResource(R.drawable.impl1);
        }

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpdatePackImage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String levelName = binding.levelET.getText().toString().trim();
                String perOrder = binding.perOrderET.getText().toString().trim();
                String sellPrice = binding.sellingET.getText().toString().trim();




                if (TextUtils.isEmpty(levelName)){
                    binding.levelET.setError("Field can't be empty!");
                }else if (TextUtils.isEmpty(perOrder)){
                    binding.perOrderET.setError("Field can't be empty!");
                }else if (TextUtils.isEmpty(sellPrice)){
                    binding.sellingET.setError("Field can't be empty!");
                }else if (imageUri == null){
                    gotoDatabase();

                }else {
                    uploadStorage();
                }
            }
        });

    }

    private void gotoDatabase() {
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("levelName",""+levelPack);
        hashMap.put("perOrderQuantity",""+perOrderPack);
        hashMap.put("sellingPrice",""+sellPrice);

        DatabaseReference myDB = FirebaseDatabase.getInstance().getReference().child("PackageInfo");
        myDB.child(idPack).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePackImage.this, "Your info has been updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadStorage() {
        progressDialog.show();
        String pathAndName = "Package_images_update/" + "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(pathAndName);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUrl = uriTask.getResult();
                if (uriTask.isSuccessful()){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("levelName",""+levelPack);
                    hashMap.put("packImage",""+downloadUrl);
                    hashMap.put("perOrderQuantity",""+perOrderPack);
                    hashMap.put("sellingPrice",""+sellPrice);

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("PackageInfo");
                    db.child(idPack).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdatePackImage.this, "Your info has been updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            assert data != null;
            imageUri = data.getData();

            try {
                binding.imagePack.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, ""+ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }
}