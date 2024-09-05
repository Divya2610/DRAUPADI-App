package com.example.draupadi;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_in extends AppCompatActivity {

    private EditText email, password;
    private CheckBox rememberMe;
    private Button loginButton;
    private TextView forgetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI components
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.rememberMe);
        loginButton = findViewById(R.id.loginButton);
        forgetPassword = findViewById(R.id.forgetPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle password reset
                resetPassword();
            }
        });
    }

    private void loginUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Enter password");
            password.requestFocus();
            return;
        }

        // Sign in with email and password
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(sign_in.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to home page or main activity
                        startActivity(new Intent(sign_in.this, MainActivity.class));
                        finish();
                    } else {
                        // If sign-in fails, display a message to the user
                        Toast.makeText(sign_in.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetPassword() {
        String emailText = email.getText().toString().trim();

        if (TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        // Send password reset email
        mAuth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to send password reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
