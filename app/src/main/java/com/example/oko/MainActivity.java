//package com.example.oko;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.SearchView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.oko.model.User;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    TextView username, loadingMsg;
//    Button loginBtn;
//    String userId;
//    User user;
//    //    ArrayList<Product>products = new ArrayList<>();
//    RecyclerView recyclerView;
////    ProductAdapter productAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        username = findViewById(R.id.greetingsText);
////        loginBtn = findViewById(R.id.btn_login);
////        loadingMsg = findViewById(R.id.loading_msg);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
//        userId = sharedPreferences.getString("UserID", null);
//        Log.e("Main", userId);
//        getUserById(userId);
//        getAllProduct();
//
//        // search
////        SearchView searchView = findViewById(R.id.search_bar);
////        search(searchView);
//
////        loginBtn.setOnClickListener(e->{
////            Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
////            startActivity(toLogin);
////        });
//
//    }
//
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
//
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
//}