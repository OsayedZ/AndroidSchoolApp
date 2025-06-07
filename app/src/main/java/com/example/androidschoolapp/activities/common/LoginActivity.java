package com.example.androidschoolapp.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.registrar.RegistrarDashboardActivity;
import com.example.androidschoolapp.activities.teacher.TeacherDashboardActivity;
import com.example.androidschoolapp.activities.student.StudentDashboardActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;
    private Chip studentChip, teacherChip, adminChip;
    
    public LoginActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_login, R.id.login_root);

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
        studentChip.setOnClickListener(v -> login("student@school.com", "student123"));
        teacherChip.setOnClickListener(v -> login("teacher@school.com", "teacher123"));
        adminChip.setOnClickListener(v -> login("registrar@school.com", "registrar123"));
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

    private Class getClassForRole(String role) {
        switch (role.toLowerCase()) {
            default:
            case "student":
                return StudentDashboardActivity.class;
            case "teacher":
                return TeacherDashboardActivity.class;
            case "registrar":
                return RegistrarDashboardActivity.class;
        }
    }

    private void login(String email, String password) {

        startLoading();
        apiClient.login(email, password,
                new ApiClient.DataCallback<String>() {
                    @Override
                    public void onSuccess(String role) {
                        showToast("Login successful!");
                        // Navigate to appropriate dashboard based on role
                        Intent intent = new Intent(LoginActivity.this, getClassForRole(role));
                        intent.putExtra("USER_EMAIL", email);
                        startActivity(intent);
                        finish();
                        endLoading();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        resetLoginButton();
                        endLoading();
                    }
                }
        );
    }

    private void resetLoginButton() {
        loginButton.setEnabled(true);
        loginButton.setText("Sign In");
    }
}

