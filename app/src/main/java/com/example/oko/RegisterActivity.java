package com.example.oko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText firstNameField, lastNameField, emailField, passwordField, confirmPasswordField;
    private Button registerButton, googleButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameField = findViewById(R.id.ETFirstName);
        lastNameField = findViewById(R.id.ETLastName);
        emailField = findViewById(R.id.emailFormField);
        passwordField = findViewById(R.id.passwordFormField);
        confirmPasswordField = findViewById(R.id.ConfirmPasswordFormField);
        registerButton = findViewById(R.id.registerButton);
        googleButton = findViewById(R.id.buttonGoogle);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (firstName.length() < 3 || lastName.length() < 3) {
            Toast.makeText(RegisterActivity.this, "First name and last name must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(RegisterActivity.this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(RegisterActivity.this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RegisterActivity.this, SubscriptionActivity.class);
        startActivity(intent);
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(RegisterActivity.this, cls));
    }
}
