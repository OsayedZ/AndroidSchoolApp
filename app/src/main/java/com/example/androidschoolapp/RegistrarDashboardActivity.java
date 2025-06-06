package com.example.androidschoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrarDashboardActivity extends AppCompatActivity {

    private CardView manageClassesCard, manageStudentsCard, manageTeachersCard;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrar_dashboard_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        
        // Get email from intent
        String email = getIntent().getStringExtra("USER_EMAIL");
        if (email != null) {
            welcomeText.setText("Welcome, " + email + "!");
        }
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        manageClassesCard = findViewById(R.id.manage_classes_card);
        manageStudentsCard = findViewById(R.id.manage_students_card);
        manageTeachersCard = findViewById(R.id.manage_teachers_card);
    }

    private void setupClickListeners() {
        manageClassesCard.setOnClickListener(v -> openManageClassesActivity());
        manageStudentsCard.setOnClickListener(v -> openManageStudentsActivity());
        manageTeachersCard.setOnClickListener(v -> openManageTeachersActivity());
    }

    private void openManageClassesActivity() {
//        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarClassesActivity.class);
//        startActivity(intent);
    }

    private void openManageStudentsActivity() {
//        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarStudentsActivity.class);
//        startActivity(intent);
    }

    private void openManageTeachersActivity() {
//        Intent intent = new Intent(RegistrarDashboardActivity.this, RegistrarTeachersActivity.class);
//        startActivity(intent);
    }
}
