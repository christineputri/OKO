package com.example.oko;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private static final String PREF_NAME = "CardDetails";
    private static final String PREF_CARDHOLDER_NAME = "CardholderName";
    private static final String PREF_CARD_NUMBER = "CardNumber";
    private static final String PREF_CVV = "CVV";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button payNowButton = findViewById(R.id.pay);
        final CheckBox rememberCheckbox = findViewById(R.id.remember);

        // Retrieve stored card details and pre-fill the fields
        if (rememberCheckbox.isChecked()) {
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            String savedCardholderName = sharedPreferences.getString(PREF_CARDHOLDER_NAME, "");
            String savedCardNumber = sharedPreferences.getString(PREF_CARD_NUMBER, "");
            String savedCvv = sharedPreferences.getString(PREF_CVV, "");

            // Pre-fill the fields
            ((EditText) findViewById(R.id.cardholder_name)).setText(savedCardholderName);
            ((EditText) findViewById(R.id.card_number)).setText(savedCardNumber);
            ((EditText) findViewById(R.id.cvv)).setText(savedCvv);
        }

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardholderName = ((EditText) findViewById(R.id.cardholder_name)).getText().toString().trim();
                String cardNumber = ((EditText) findViewById(R.id.card_number)).getText().toString().trim();
                String cvv = ((EditText) findViewById(R.id.cvv)).getText().toString().trim();

                if (cardholderName.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PaymentActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    finish(); // Optional, to close the current activity if needed

                    if (rememberCheckbox.isChecked()) {
                        // Save card details
                        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                        editor.putString(PREF_CARDHOLDER_NAME, cardholderName);
                        editor.putString(PREF_CARD_NUMBER, cardNumber);
                        editor.putString(PREF_CVV, cvv);
                        editor.apply();
                    }
                }
            }
        });
    }
}


