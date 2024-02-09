package com.example.oko;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oko.databinding.ActivityMainBinding;
import com.example.oko.model.User;

import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    //    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int REQUEST_CAMERA_PERMISSION = 1001;
    private static final int REQUEST_AUDIO_PERMISSION = 1002;
    TextView username, loadingMsg;
    Button loginBtn;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ActivityMainBinding binding;
    TextToSpeech tts;
    String apiKey;
    private ImageAnalysis imageAnalysis;
    ImageCapture imageCapture;
    String geminiCommand = "";
    Dialog waitDialog;

    Bitmap image1;

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

        apiKey = "AIzaSyBDetIe0k4mATyPJ_4pa5P0e6dKcV3cnUs";
        waitDialog = new Dialog(this);
        waitDialog.setContentView(R.layout.wait_dialog);
        waitDialog.setCancelable(false);
        waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        waitDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.getSpeechBtn.setOnClickListener(view -> {
            speechRecog();
            capturePhoto();
        });

        binding.viewFinder.setOnClickListener(view -> {
            speechRecog();
            capturePhoto();
        });

        ttsListener();
//        camViewfinder();
        checkPermission();
        clickListener();

    }

    private void clickListener(){
        binding.objDetect.setOnClickListener(view -> {
            tts.speak("Say, what's in fron of me", TextToSpeech.QUEUE_FLUSH, null);
        });
        binding.tts.setOnClickListener(view -> {
            tts.speak("Say, read it for me", TextToSpeech.QUEUE_FLUSH, null);
        });
        binding.facialRecog.setOnClickListener(view -> {
            tts.speak("Say, who's in front of me", TextToSpeech.QUEUE_FLUSH, null);
        });
        binding.gestureRecog.setOnClickListener(view -> {
            tts.speak("gesture and hand sign recognition coming soon", TextToSpeech.QUEUE_FLUSH, null);
        });

        binding.profileBtn.setOnClickListener(view -> {
            tts.speak("Opening My Profile Page", TextToSpeech.QUEUE_FLUSH, null);
//            startActivity(new Intent(this, ProfileA));
        });

        binding.subBtn.setOnClickListener(view -> {
            tts.speak("Opening Subscription Page", TextToSpeech.QUEUE_FLUSH, null);
            startActivity(new Intent(this, SubscriptionActivity.class));
        });

        binding.connectedDeviceBtn.setOnClickListener(view -> {
            tts.speak("Connected glasses currently only accessible by developer", TextToSpeech.QUEUE_FLUSH, null);
//            startActivity(new Intent(this, ));
        });
    }

    private void gemini(Uri imag) {

//
//        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();


        if (geminiCommand.startsWith("help") || geminiCommand.equals("help me")){
//
        }else{
            GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro-vision", apiKey);

            GenerativeModelFutures model = GenerativeModelFutures.from(gm);

            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imag));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Content content = new Content.Builder()
                    .addText(geminiCommand)
                    .addImage(bitmap)
                    .build();

            Executor executor = Executors.newSingleThreadExecutor();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
//                System.out.println(resultText);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                        Toast.makeText(MainActivity.this, resultText, Toast.LENGTH_SHORT).show();
                            tts.speak(resultText, TextToSpeech.QUEUE_ADD, null);
                            waitDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, executor);

        }



    }

    private void ttsListener() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
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

                } catch (NullPointerException e) {
                }

            }

        };
        timer.start();
    }

    private void speechRecog() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        startActivityIfNeeded(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            String command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
//            Toast.makeText(this, command, Toast.LENGTH_SHORT).show();
            if (command.equals("help") || command.equals("help me")) {
                String instruction = "Say, What's in front of me? to know your surrounding. " +
                        "Say, Read this text! to read a text. " +
                        "Say, Who's in front of me? to know a person name in front of you";
                waitDialog.dismiss();
                tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
            } else if (command.equals("who's in front of me") || command.equals("who is in front of me") || command.equals("who is it") || command.equals("who is that")  || command.equals("who is this")) {

                startActivity(new Intent(this, FaceRecognitionActivity.class));
                waitDialog.dismiss();
            } else {
                geminiCommand = command;
            }
        }
    }

    private void camViewfinder() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
            }
        }, ContextCompat.getMainExecutor(this));
    }


    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
    }

    private void capturePhoto() {
        long timeStamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");


        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(MainActivity.this,"processing...",Toast.LENGTH_SHORT).show();

                        Uri savedImageUri = outputFileResults.getSavedUri();
                        waitDialog.show();

                        if (geminiCommand.equals("help") || geminiCommand.equals("help me") || geminiCommand.equals("help ")){

                        }else{

                            gemini(savedImageUri);
                        }

                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(MainActivity.this,"Error: "+exception.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                });

    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
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


    private void checkPermission(){

        if (hasCameraPermission() && hasAudioPermission()) {
            camViewfinder();
        } else {
            requestPermissions();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasAudioPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, check audio permission
                if (hasAudioPermission()) {
                    camViewfinder();
                } else {
                    requestAudioPermission();
                }
            } else {
                // Camera permission denied
                // Handle the case where the user denies camera permission
            }
        } else if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Audio permission granted, initialize the camera
                camViewfinder();
            } else {
                // Audio permission denied
                // Handle the case where the user denies audio permission
            }
        }
    }

    private void requestAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_AUDIO_PERMISSION);
        }
    }


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