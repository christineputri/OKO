package com.example.oko;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oko.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity {

    ActivitySubscriptionBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.standartPlan.setOnClickListener(view -> {
            startActivity();
        });

        binding.basicPlan.setOnClickListener(view -> {
            startActivity();
        });

        binding.proPlan.setOnClickListener(view -> {
            startActivity();
        });
    }

    void startActivity(){startActivity(new Intent(SubscriptionActivity.this, PaymentActivity.class));}
}
