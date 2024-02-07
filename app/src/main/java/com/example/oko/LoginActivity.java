package com.example.oko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oko.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton, continueWithGoogleButton;

    // ArrayList to store email and password pairs
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailFormField);
        passwordEditText = findViewById(R.id.passwordFormField);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerPageButton);
        continueWithGoogleButton = findViewById(R.id.buttonGoogle);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to RegisterActivity
                startActivity(RegisterActivity.class);
            }
        });

        continueWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Directly go to SubscriptionActivity for Google login
                startActivity(SubscriptionActivity.class);
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                // Successful login
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Redirect to SubscriptionActivity
                startActivity(SubscriptionActivity.class);
                return;
            }
        }

        // Invalid credentials
        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(LoginActivity.this, cls));
    }
}
