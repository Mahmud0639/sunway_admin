package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manuni.sunwayadmin.databinding.ActivityPackageUploadBinding;

import java.util.HashMap;
import java.util.Objects;

public class PackageUpload extends AppCompatActivity {
    ActivityPackageUploadBinding binding;
    private ProgressDialog progressDialog;
    private Uri imageUri;

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


        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });
    }

    private void showImagePickDialog() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();

            try {
                binding.imagePack.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "" + ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }

    }

    private void checkValidation() {
        String level = binding.levelET.getText().toString().trim();
        String perOrder = binding.perOrderET.getText().toString().trim();
        String sellPrice = binding.sellingET.getText().toString().trim();

        if (level.isEmpty()){
            binding.levelET.setError("Field can't be empty.");
            return;
        }
        if (perOrder.isEmpty()){
            binding.perOrderET.setError("Field can't be empty.");
            return;
        }
        if (sellPrice.isEmpty()){
            binding.sellingET.setError("Field can't be empty.");
            return;
        }

        uploadToDatabase(level,perOrder,sellPrice);
    }

    private void uploadToDatabase(String myLevel,String myPerOrder,String sellingPrice) {
        binding.levelET.setText("");
        binding.perOrderET.setText("");
        progressDialog.show();

        DatabaseReference packDB = FirebaseDatabase.getInstance().getReference().child("PackageInfo");


        if (imageUri==null){

            String key = packDB.push().getKey();

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("levelName",""+myLevel);
            hashMap.put("perOrderQuantity",""+myPerOrder);
            hashMap.put("packImage","");
            hashMap.put("sellingPrice",sellingPrice);
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
        }else {
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            String filePathAndName = "packTaskImages/"+System.currentTimeMillis();
            ref.child(filePathAndName).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful());
                Uri downloadUrl = uriTask.getResult();

                String key = packDB.push().getKey();

                if (uriTask.isSuccessful()){
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("levelName",""+myLevel);
                    hashMap.put("perOrderQuantity",""+myPerOrder);
                    hashMap.put("packImage",""+downloadUrl);
                    hashMap.put("sellingPrice",sellingPrice);
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

            });
        }




    }
}