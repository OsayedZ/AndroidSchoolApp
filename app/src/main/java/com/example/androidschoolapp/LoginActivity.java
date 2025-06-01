package com.example.androidschoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailInputLayout = findViewById(R.id.email_input_layout);
        passwordInputLayout = findViewById(R.id.password_input_layout);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());

    }

    private void attemptLogin() {
        // Clear previous errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        boolean isValid = true;

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailInputLayout.setError("Please enter a valid email");
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (isValid) {
            performLogin(email, password);
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void performLogin(String email, String password) {
        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Signing in...");

        // TODO: Replace with actual authentication logic
        simulateLogin(email, password);
    }

    private void simulateLogin(String email, String password) {
        // Simulate network delay
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Demo credentials (replace with actual authentication)
                if (isValidCredentials(email, password)) {
                    // Login successful
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    resetLoginButton();
                }
            }
        }, 1500);
    }

    private boolean isValidCredentials(String email, String password) {
         return (email.equals("student@school.com") && password.equals("student123")) ||
               (email.equals("teacher@school.com") && password.equals("teacher123")) ||
               (email.equals("admin@school.com") && password.equals("admin123"));
    }

    private void resetLoginButton() {
        loginButton.setEnabled(true);
        loginButton.setText("Sign In");
    }

} 