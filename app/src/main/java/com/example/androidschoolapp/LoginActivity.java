package com.example.androidschoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;
    private Chip studentChip, teacherChip, adminChip;

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
        studentChip = findViewById(R.id.student_chip);
        teacherChip = findViewById(R.id.teacher_chip);
        adminChip = findViewById(R.id.admin_chip);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        studentChip.setOnClickListener(v-> login("student@school.com", "student123"));
        teacherChip.setOnClickListener(v-> login("teacher@school.com", "teacher123"));
        adminChip.setOnClickListener(v-> login("registrar@school.com", "registrar123"));
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

        login(email, password);
    }

    private Class getClassForRole(String email) {
        switch(email)
        {
            default:
            case "student@school.com":
                return StudentDashboardActivity.class;
            case "teacher@school.com":
                return TeacherDashboardActivity.class;
            case "registrar@school.com":
                return RegistrarDashboardActivity.class;
        }
    }
    private void login(String email, String password) {
        // TODO: Replace with actual authentication logic

        if (isValidCredentials(email, password)) {
            // Login successful
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, getClassForRole(email));
            intent.putExtra("USER_EMAIL", email);
            startActivity(intent);

            finish();
        } else {
            // Login failed
            Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
            resetLoginButton();
        }
    }

    private boolean isValidCredentials(String email, String password) {
        return (email.equals("student@school.com") && password.equals("student123")) ||
                (email.equals("teacher@school.com") && password.equals("teacher123")) ||
                (email.equals("registrar@school.com") && password.equals("registrar123"));
    }

    private void resetLoginButton() {
        loginButton.setEnabled(true);
        loginButton.setText("Sign In");
    }

} 