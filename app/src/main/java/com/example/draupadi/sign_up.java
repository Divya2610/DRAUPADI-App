package com.example.draupadi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_up extends AppCompatActivity {

    private EditText username, email, password, confirmPassword, mobile;
    private Button logupButton, signinButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI components
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        mobile = findViewById(R.id.mobile);
        logupButton = findViewById(R.id.logupButton);
        signinButton = findViewById(R.id.signin_Button);

        logupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void signUpUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        if (!validateInput(emailText, passwordText, confirmPasswordText)) {
            return;
        }

        // Create a new user with email and password
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(sign_up.this, "Successfully signed up!", Toast.LENGTH_SHORT).show();

                        // Redirect to login page
                        Intent intent = new Intent(sign_up.this, sign_in.class);
                        startActivity(intent);
                        finish();  // To prevent going back to the sign-up page on back press
                    } else {
                        Toast.makeText(sign_up.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter valid email");
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
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to home page or main activity
                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(String emailText, String passwordText, String confirmPasswordText) {
        if (TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter valid email");
            email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(passwordText) || passwordText.length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return false;
        }
        if (!passwordText.equals(confirmPasswordText)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}
