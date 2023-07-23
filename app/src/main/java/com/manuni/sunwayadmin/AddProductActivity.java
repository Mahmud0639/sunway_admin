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
import com.manuni.sunwayadmin.databinding.ActivityAddProductBinding;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    private String proNumber,proSellPrice,paId,perOrder;

    private ProgressDialog progressDialog;


    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        paId = getIntent().getStringExtra("packageId");
        perOrder = getIntent().getStringExtra("perOrder");

        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Data adding...");

        binding.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proNumber = binding.productNumber.getText().toString().trim();
                proSellPrice = binding.sellPriceEt.getText().toString().trim();

                if (TextUtils.isEmpty(proNumber)){
                    binding.productNumber.setError("Field can't be empty!");
                    binding.productNumber.requestFocus();
                }else if (TextUtils.isEmpty(proSellPrice)){
                    binding.sellPriceEt.setError("Field can't be empty!");
                    binding.sellPriceEt.requestFocus();
                }else if (imageUri == null){
                    Toast.makeText(AddProductActivity.this, "An image is required to further proceed.", Toast.LENGTH_SHORT).show();
                }else {
                    gotoStorage();
                }
            }
        });

        binding.showProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this,ShowProductsActivity.class);
                intent.putExtra("packId",""+paId);
                startActivity(intent);
                //startActivity(new Intent(AddProductActivity.this,ShowProductsActivity.class));
            }
        });


        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddProductActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });



    }

    private void gotoStorage(){
        progressDialog.show();
        String pathAndName = "ProductImages/" + "" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(pathAndName);

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("AllProductsPriceInfo");

                String productId = myRef.push().getKey();

                if (uriTask.isSuccessful()){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("productImage",""+downloadUri);
                    hashMap.put("productNumber",""+proNumber);
                    hashMap.put("productSellingPrice",""+proSellPrice);
                    hashMap.put("productId",""+productId);
                    hashMap.put("perOrder",""+perOrder);

                    assert productId != null;
                    myRef.child(paId).child(productId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.productNumber.setText("");
                            binding.sellPriceEt.setText("");

                            progressDialog.dismiss();

                            Toast.makeText(AddProductActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
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