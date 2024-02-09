package com.example.oko;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oko.databinding.ActivityMainBinding;
import com.example.oko.model.User;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.common.util.concurrent.ListenableFuture;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

//    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView username, loadingMsg;
    Button loginBtn;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ActivityMainBinding binding;
    TextToSpeech tts;

    String userId;
    User user;
    //    ArrayList<Product>products = new ArrayList<>();
    RecyclerView recyclerView;
//    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = findViewById(R.id.greetingsText);
//        loginBtn = findViewById(R.id.btn_login);
//        loadingMsg = findViewById(R.id.loading_msg);

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        userId = sharedPreferences.getString("UserID", null);
//        Log.e("Main", userId);
//        getUserById(userId);
//        getAllProduct();

        // search
//        SearchView searchView = findViewById(R.id.search_bar);
//        search(searchView);

//        loginBtn.setOnClickListener(e->{
//            Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(toLogin);
//        });


        binding.getSpeechBtn.setOnClickListener(view -> {
            speechRecog();
        });

        binding.viewFinder.setOnClickListener(view -> {
            speechRecog();
        });

        ttsListener();
        camViewfinder();

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

        String hello = "Hello, welcome to OKkO. please wear an earphone for clearer sound. " +
                "Press the big red button or in the middle of your screen to speak. " +
                "Say help, to know all of the commands";

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

    private void speechRecog(){

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
            startActivityIfNeeded(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){
            String command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(this, command, Toast.LENGTH_SHORT).show();
            if (command.equals("help")){
                String instruction = "Say, What's in front of me? to know your surrounding. " +
                        "Say, Read this text! to read a text. " +
                        "Say, Who's in front of me? to know a person name in front of you";
                tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
            }
            else if (command.equals("what's in front of me") || command.equals("what is in front of me")){

                tts.speak("detecting...", TextToSpeech.QUEUE_ADD, null);
            }
            else if (command.equals("read this text") || command.equals("read this")){

                tts.speak("detecting...", TextToSpeech.QUEUE_ADD, null);
            }
            else if (command.equals("who's in front of me") || command.equals("who is in front of me") || command.equals("who is it")){

                startActivity(new Intent(this, FaceRecognitionActivity.class));
            }
            else{
                // use gemini
            }
        }
    }

    private void camViewfinder(){

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int menuItem = item.getItemId();
//
//        if (menuItem == R.id.action_profile) {
//            Intent toLogin = new Intent(this, ProfileActivity.class);
//            startActivity(toLogin);
//        } else if (menuItem == R.id.action_sign_out) {
//            Intent toLogin = new Intent(this, LoginActivity.class);
//            startActivity(toLogin);
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    public void getUserById(String id) {
//        ArrayList<User> users = new ArrayList<>();
//        db.collection("users").document(id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists() && !document.getData().isEmpty()) {
//                                user = document.toObject(User.class);
//                                user.setId(document.getId());
//                                username.setText("Hello, " + user.getFirstName() + " " + user.getLastName() + "!");
//                            } else {
//                                Log.e("OKO", "No such document");
//                            }
//                        } else {
//                            Log.e("OKO", "get failed with ", task.getException());
//                        }
//                    }
//                });
//    }


    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tts.stop();
    }
}