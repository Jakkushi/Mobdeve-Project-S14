package com.mobdeve.s14.group20.mobdeveproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private ProgressBar pbRegister;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

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
        setContentView(R.layout.activity_register);

        this.hideUI();
        this.initFirebase();
        this.initComponents();
    }

    private void initFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
    }

    private void initComponents(){
        this.tvLogin = findViewById(R.id.tv_register_login);
        this.etEmail = findViewById(R.id.et_register_email);
        this.etPassword = findViewById(R.id.et_register_password);
        this.btnRegister = findViewById(R.id.btn_register_submit);
        this.pbRegister = findViewById(R.id.pb_register_creds);

        this.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(!checkEmpty(email, password)) {
                    String newpassword = computeMD5Hash(password);
                    Log.d("NEWPASS", newpassword);

                    User user = new User(email, newpassword);
                    storeUser(user);
                }
            }
        });
    }

    private boolean checkEmpty(String email, String password) {
        boolean hasEmpty = false;

        if(email.isEmpty()){
            this.etEmail.setError("This is a required field");
            this.etEmail.requestFocus();
            hasEmpty = true;
        }

        if(password.isEmpty()){
            this.etPassword.setError("This is a required field");
            this.etPassword.requestFocus();
            hasEmpty = true;
        }

        return hasEmpty;
    }

    private void storeUser(User user) {
        this.pbRegister.setVisibility(View.VISIBLE);

        System.out.println(user.getEmail());

        this.mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            database.getReference(Collection.users.name())
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        successfulRegistration();
                                    }
                                    else {
                                        failedRegistration();
                                    }
                                }
                            });

                        }
                        else {
                            failedRegistration();
                        }
                    }
                });
    }

    public String computeMD5Hash(String password){

        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for(int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                MD5Hash.append(h);
            }

            return MD5Hash.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void successfulRegistration() {
        this.pbRegister.setVisibility(View.GONE);
        Toast.makeText(this, "User Successfully Registered", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(new Intent(RegisterActivity.this, MainActivity.class));
        startActivity(intent);
        finish();
    }

    private void failedRegistration() {
        this.pbRegister.setVisibility(View.GONE);
        Toast.makeText(this, "User Registration Failed", Toast.LENGTH_SHORT).show();
    }
}