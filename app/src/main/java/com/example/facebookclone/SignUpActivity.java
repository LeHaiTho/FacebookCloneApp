package com.example.facebookclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

//    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tiến trình đăng ký
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Đăng ký...");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        // hành động đăng ký
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lấy dữ liệu truyền vào từ edit text
                String userName = Objects.requireNonNull(binding.userNameTxt.getText()).toString().trim();
                String email = Objects.requireNonNull(binding.emailEdt.getText()).toString().trim();
                String password = Objects.requireNonNull(binding.passwordEdt.getText()).toString().trim();

                // kiểm tra du lieu
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEdt.setError("Invalid email");
                    binding.emailEdt.setFocusable(true);
                } else if (password.length() < 6) {
                    binding.passwordEdt.setError("Mật khẩu phải ít nhất 6 ký tự");
                    binding.passwordEdt.setFocusable(true);
                } else {
                    signUpUser(userName,email, password);
                }

            }
        });

        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUpUser(String userName,String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            // add data in UserModel
                            UserModel user = new UserModel(userName, email, password);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this, "User Data saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}