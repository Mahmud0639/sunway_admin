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
import com.manuni.sunwayadmin.databinding.ActivityAddSliderImageBinding;

import java.util.HashMap;

public class AddSliderImageActivity extends AppCompatActivity {
    ActivityAddSliderImageBinding binding;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSliderImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(AddSliderImageActivity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

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

    private void checkValidation() {


        String title = binding.titleET.getText().toString().trim();

        if (imageUri==null){
            Toast.makeText(this, "Please select an image to upload.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (title.isEmpty()){
            Toast.makeText(this, "Write your title.", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImage(title);

    }

    private void uploadImage(String titleTxt) {
        binding.submitBtn.setEnabled(false);
        progressDialog.show();

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        String filePathAndName = "sliderImages/"+System.currentTimeMillis();
        ref.child(filePathAndName).putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

            while (!uriTask.isSuccessful());
            Uri downloadUrl = uriTask.getResult();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SliderImages");

            String key = databaseReference.push().getKey();

            if (uriTask.isSuccessful()){
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("id",""+key);
                hashMap.put("slider",""+downloadUrl);
                hashMap.put("title",""+titleTxt);


                databaseReference.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(AddSliderImageActivity.this, "Data added successfully.", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddSliderImageActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
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
}