package com.example.androidschoolapp.activities.registrar;

import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class RegistrarDashboardActivity extends BaseActivity {

    private CardView manageClassesCard, manageStudentsCard, manageTeachersCard;
    private TextView welcomeText;
    
    public RegistrarDashboardActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_dashboard, R.id.registrar_dashboard_root);

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
