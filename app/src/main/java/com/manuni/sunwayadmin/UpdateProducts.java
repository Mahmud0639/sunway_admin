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
import com.manuni.sunwayadmin.databinding.ActivityUpdateProductsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateProducts extends AppCompatActivity {

    ActivityUpdateProductsBinding binding;
    private String proNumber,sellPrice,proImage,proId,packId;
    private Uri imageUri;

    private ProgressDialog dialog;

    private DatabaseReference dbR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        proNumber = getIntent().getStringExtra("productNumber");
        sellPrice = getIntent().getStringExtra("sellingPrice");
        proImage = getIntent().getStringExtra("productImage");
        proId = getIntent().getStringExtra("productId");
        packId = getIntent().getStringExtra("packId");



        binding.productNumber.setText(proNumber);
        binding.sellingET.setText(sellPrice);

        try {
            Picasso.get().load(proImage).into(binding.imagePack);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dialog = new ProgressDialog(UpdateProducts.this);
        dialog.setMessage("Updating...");

        dbR = FirebaseDatabase.getInstance().getReference();

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String prNumber = binding.productNumber.getText().toString().trim();
                String sellingPrice = binding.sellingET.getText().toString().trim();

                if (TextUtils.isEmpty(prNumber)){
                    binding.productNumber.setError("Field can't be empty.");
                }else if (TextUtils.isEmpty(sellingPrice)){
                    binding.sellingET.setError("Field can't be empty.");
                }else if (imageUri == null){

                    dialog.show();

                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("productNumber",""+prNumber);
                    hashMap.put("productSellingPrice",""+sellingPrice);

                    dbR.child("AllProductsPriceInfo").child(packId).child(proId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UpdateProducts.this, "Product info updated.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else{

                    dialog.show();
                    String pathAndName = "Product_images_update/" + "" + System.currentTimeMillis();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(pathAndName);
                    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();

                            if (uriTask.isSuccessful()){
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("productImage",""+downloadUrl);
                                hashMap.put("productNumber",""+prNumber);
                                hashMap.put("productSellingPrice",""+sellingPrice);

                                dbR.child("AllProductsPriceInfo").child(packId).child(proId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UpdateProducts.this, "Product info updated.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpdateProducts.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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