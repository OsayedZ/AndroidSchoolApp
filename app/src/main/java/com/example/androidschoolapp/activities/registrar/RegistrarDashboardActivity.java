package com.example.androidschoolapp.activities.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class RegistrarDashboardActivity extends BaseActivity {

    private CardView manageClassesCard, manageSubjectsCard, manageStudentsCard, manageTeachersCard;
    private TextView welcomeText, userEmailText;

    public RegistrarDashboardActivity() {
        super("Dashboard");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_dashboard, R.id.registrar_dashboard_root);

        initializeViews();
        setupClickListeners();
        setupUserInfo();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        userEmailText = findViewById(R.id.user_email_text);
        manageClassesCard = findViewById(R.id.manage_classes_card);
        manageSubjectsCard = findViewById(R.id.manage_subjects_card);
        manageStudentsCard = findViewById(R.id.manage_students_card);
        manageTeachersCard = findViewById(R.id.manage_teachers_card);
    }

    private void setupClickListeners() {
        manageClassesCard.setOnClickListener(v -> openManageClassesActivity());
        manageSubjectsCard.setOnClickListener(v -> openManageSubjectsActivity());
        manageStudentsCard.setOnClickListener(v -> openManageStudentsActivity());
        manageTeachersCard.setOnClickListener(v -> openManageTeachersActivity());
    }

    private void setupUserInfo() {
        // Get email from session or intent
        String email = sessionManager.getEmail();
        if (email == null || email.isEmpty()) {
            email = getIntent().getStringExtra("USER_EMAIL");
        }
        
        if (email != null && !email.isEmpty()) {
            userEmailText.setText(email);
            // Extract name from email for welcome message
            String name = email.split("@")[0];
            String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
            welcomeText.setText("Welcome back, " + capitalizedName + "!");
        } else {
            welcomeText.setText("Welcome back!");
            userEmailText.setText("");
        }
    }

    private void openManageClassesActivity() {
        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarClassesActivity.class);
        startActivity(intent);
    }

    private void openManageSubjectsActivity() {
        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarSubjectsActivity.class);
        startActivity(intent);
    }

    private void openManageStudentsActivity() {
        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarStudentsActivity.class);
        startActivity(intent);
    }

    private void openManageTeachersActivity() {
        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarTeachersActivity.class);
        startActivity(intent);
    }
}
