package com.mobdeve.s14.group20.mobdeveproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button clearButton;
    private Button saveButton;
    private ProgressBar pbLogin;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private CanvasView canvas;

    private TextView titleView;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private String userId;

    public ArrayList<DatabaseNotesData> dbNotes = new ArrayList<>();
    boolean withChild = false;

    private void hideUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideUI();
        this.initFirebase();
        this.initComponents();

//        context = getApplicationContext();
//        clearButton = findViewById(R.id.sketch_clear_screen);
//        saveButton = findViewById(R.id.sketch_save_button);
//        canvas = findViewById(R.id.sketch_canvas_view);
//        titleView = findViewById(R.id.sketch_tv_title);
//
//        clearButton.setOnClickListener(v -> canvas.clearScreen());
//        saveButton.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                CharSequence text;
//                try {
//                    canvas.saveScreen(titleView.getText());
//                    text = "Sketch saved successfully!";
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    text = "Unable to save sketch";
//                }
//                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//                toast.show();
//            } else {
//                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//        });
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    private void signIn(String email, String password){
        this.pbLogin.setVisibility(View.VISIBLE);
        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            userId = user.getUid();
                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference().child(Collection.users.name());

                            withChild = false;
                            getNotesData();
                            Toast.makeText(MainActivity.this, "Welcome! Retrieving your notes...", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            pbLogin.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "User Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initComponents(){
        this.pbLogin = findViewById(R.id.pb_main);
        this.tvRegister = findViewById(R.id.tv_main_register);
        this.etEmail = findViewById(R.id.et_login_email);
        this.etPassword = findViewById(R.id.et_login_password);
        this.btnLogin = findViewById(R.id.btn_login_submit);

        this.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.d("Error", "Keyboard not open");
                }

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(!checkEmpty(email, password)) {
                    signIn(email, password);
                }
            }
        });

    }

    private boolean checkEmpty(String email, String password) {
        boolean hasEmpty = false;

        if(email.isEmpty()){
            this.etEmail.setError("Required Field");
            this.etEmail.requestFocus();
            hasEmpty = true;
        }

        if(password.isEmpty()){
            this.etPassword.setError("Required Field");
            this.etPassword.requestFocus();
            hasEmpty = true;
        }

        return hasEmpty;
    }

    private void getNotesData(){

        this.reference.child((this.userId)).child(Collection.notes.name()).orderByChild(Collection.dateModified.name()).startAt("2019-01-01").endAt("2021-12-31").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                withChild = true;

                ArrayList<ArrayList<String>> interestItems = new ArrayList<>();
                ArrayList<String> tags = new ArrayList<>();
                ArrayList<ArrayList<String>> todoList = new ArrayList<>();
                ArrayList<ArrayList<String>> blankItems = new ArrayList<>();

                try {
                    interestItems =  (ArrayList) (( (HashMap) snapshot.getValue()).get("interestItem"));
                }
                catch (Exception e){
                    interestItems = null;
                    Log.w("error", "No Interest items in entry");
                }

                try {
                    tags = (ArrayList) (((HashMap) snapshot.getValue()).get("tags"));
                }
                catch (Exception d){
                    tags = null;
                    Log.w("error", "No Tags in entry");
                }

                try {
                    todoList = (ArrayList) (( (HashMap) snapshot.getValue()).get("todo"));
                } catch (Exception f) {
                    todoList = null;
                    Log.w("error", "No Todos in entry");
                }

                try {
                    blankItems = (ArrayList) (((HashMap) snapshot.getValue()).get("blankItems"));
                    Log.w("blank items", String.valueOf(blankItems.get(0).get(0).getClass()));
                    //first .get(0) returns Arraylist<String>
                    //second .get(0) returns String
                }
                catch (Exception d){
                    blankItems = null;
                    Log.w("error", "No Blank items in entry");
                }

                Log.d("Snapshot Key: ", snapshot.getKey());

                dbNotes.add(new DatabaseNotesData( (String) (( (HashMap) snapshot.getValue()).get("title")),
                        (String) ((HashMap) snapshot.getValue()).get("subtitle"), (String) ((HashMap) snapshot.getValue()).get("noteType"),
                        (String) ((HashMap) snapshot.getValue()).get("dateModified"), interestItems, tags, todoList, blankItems, snapshot.getKey()));

                Intent intent = new Intent(MainActivity.this, DisplayNotesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                Collections.reverse(dbNotes);
                intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                startActivity(intent);
                finish();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        this.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!withChild){
                    pbLogin.setVisibility(View.GONE);
                    Intent intent = new Intent(MainActivity.this, DisplayNotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Collections.reverse(dbNotes);
                    intent.putExtra(Keys.DBNOTES.name(), dbNotes);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    CharSequence text;
                    try {
                        canvas.saveScreen(titleView.getText());
                        text = "Sketch saved successfully!";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        text = "Unable to save sketch";
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Unable to save image without permissions.", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
}