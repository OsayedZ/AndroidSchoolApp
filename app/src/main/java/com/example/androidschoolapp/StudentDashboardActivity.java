package com.example.androidschoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StudentDashboardActivity extends AppCompatActivity {

    private CardView tasksCard, scheduleCard, gradesCard, submissionsCard;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.student_dashboard_root), (v, insets) -> {
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
        tasksCard = findViewById(R.id.tasks_card);
        scheduleCard = findViewById(R.id.schedule_card);
        gradesCard = findViewById(R.id.grades_card);
        submissionsCard = findViewById(R.id.submissions_card);
    }

    private void setupClickListeners() {
        tasksCard.setOnClickListener(v -> openTasksActivity());
        scheduleCard.setOnClickListener(v -> openScheduleActivity());
        gradesCard.setOnClickListener(v -> openGradesActivity());
        submissionsCard.setOnClickListener(v -> openSubmissionsActivity());
    }

    private void openTasksActivity() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentTasksActivity.class);
        startActivity(intent);
    }

    private void openScheduleActivity() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentScheduleActivity.class);
        startActivity(intent);
    }

    private void openGradesActivity() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentGradesActivity.class);
        startActivity(intent);
    }

    private void openSubmissionsActivity() {
        Intent intent = new Intent(StudentDashboardActivity.this, StudentSubmissionsActivity.class);
        startActivity(intent);
    }
}
