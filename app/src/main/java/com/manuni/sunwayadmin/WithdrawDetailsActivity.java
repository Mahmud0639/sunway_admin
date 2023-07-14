package com.manuni.sunwayadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.manuni.sunwayadmin.databinding.ActivityWithdrawDetailsBinding;

public class WithdrawDetailsActivity extends AppCompatActivity {
    ActivityWithdrawDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}