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
import com.manuni.sunwayadmin.databinding.ActivityPackInfoBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PackInfoActivity extends AppCompatActivity {
    ActivityPackInfoBinding binding;
    private String paImage,paPerOrder,paLevel,paId;
    private Uri imageUri;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPackInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        paImage = getIntent().getStringExtra("pacImg");
        paPerOrder = getIntent().getStringExtra("pacPerOrder");
        paLevel = getIntent().getStringExtra("pacLevel");
        paId = getIntent().getStringExtra("pacId");

        progressDialog = new ProgressDialog(PackInfoActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Picasso.get().load(paImage).into(binding.imagePack);
        binding.levelET.setText(paLevel);
        binding.perOrderET.setText(paPerOrder);

        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
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

        DatabaseReference packDB = FirebaseDatabase.getInstance().getReference().child("TaskPackageInfo");


        if (imageUri==null){

            String key = packDB.push().getKey();

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("levelName",""+myLevel);
            hashMap.put("perOrderQuantity",""+myPerOrder);
            hashMap.put("packImage","");
            hashMap.put("id",""+key);

            packDB.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(PackInfoActivity.this, "Data added successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PackInfoActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            StorageReference ref = FirebaseStorage.getInstance().getReference();
            String filePathAndName = "packTaskInfoImages/"+System.currentTimeMillis();
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
                    hashMap.put("id",""+key);


                    packDB.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(PackInfoActivity.this, "Data added successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PackInfoActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        }




    }
}