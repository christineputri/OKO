package com.example.oko;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oko.databinding.ActivityProfileBinding;
import com.example.oko.databinding.ActivitySubscriptionBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
