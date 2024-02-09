package com.example.oko;


import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import androidx.appcompat.app.AppCompatActivity;

public class SubscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Button basicPlanButton = findViewById(R.id.basic_plan);
        Button standardPlanButton = findViewById(R.id.standart_plan);
        Button proPlanButton = findViewById(R.id.pro_plan);

        basicPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        standardPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        proPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }
}
