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

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton, continueWithGoogleButton, loginButton;

    // ArrayList to store user data
    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.ETFirstName);
        lastNameEditText = findViewById(R.id.ETLastName);
        emailEditText = findViewById(R.id.emailFormField);
        passwordEditText = findViewById(R.id.passwordFormField);
        confirmPasswordEditText = findViewById(R.id.ConfirmPasswordFormField);
        registerButton = findViewById(R.id.registerButton);
        continueWithGoogleButton = findViewById(R.id.buttonGoogle);
        loginButton = findViewById(R.id.loginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        continueWithGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Directly go to MainActivity for Google login
                startActivity(MainActivity.class);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to LoginActivity
                startActivity(LoginActivity.class);
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            // Passwords do not match, show error message
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the email is already registered
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                // Email already exists, show error message
                Toast.makeText(RegisterActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Add the new user to the list
        userList.add(new User(firstName, lastName, email, password));

        // Display success message
        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

        // Redirect to MainActivity after registration
        startActivity(MainActivity.class);
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(RegisterActivity.this, cls));
    }
}
