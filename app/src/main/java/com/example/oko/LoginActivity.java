package com.example.oko;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oko.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {


    TextToSpeech tts;
    private TextInputEditText emailFormField, passwordFormField;
    private Button loginButton, buttonGoogle, registerPageButton;

    private List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailFormField = findViewById(R.id.emailFormField);
        passwordFormField = findViewById(R.id.passwordFormField);
        loginButton = findViewById(R.id.loginButton);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        registerPageButton = findViewById(R.id.registerPageButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });

        registerPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ttsListener();
    }

    private void loginUser() {
        String email = emailFormField.getText().toString().trim();
        String password = passwordFormField.getText().toString().trim();

        // Check if email and password are not empty
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email is a Gmail address
        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(LoginActivity.this, "Please enter a valid Gmail address", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, SubscriptionActivity.class);
        startActivity(intent);
    }

    private void ttsListener(){
         tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        String hello = "Hello, welcome to OKkO. You're currently on Login Page. if you're unable to fill the form, please ask someone to help you";

//        tts.speak(hello, TextToSpeech.QUEUE_ADD, null);

        Thread timer = new Thread() {
            public void run() {
                try {
                    try {
                        sleep(1000);

                        tts.speak(hello, TextToSpeech.QUEUE_ADD, null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (NullPointerException e){}

            }

        };
        timer.start();
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(LoginActivity.this, cls));
    }
}
