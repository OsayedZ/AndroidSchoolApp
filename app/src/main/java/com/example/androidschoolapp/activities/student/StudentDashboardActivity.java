package com.example.androidschoolapp.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class StudentDashboardActivity extends BaseActivity {

    private CardView tasksCard, scheduleCard, gradesCard, submissionsCard;
    private TextView welcomeText;
    
    public StudentDashboardActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_dashboard, R.id.student_dashboard_root);

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
