package com.example.androidschoolapp.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.SessionManager;

public class StudentDashboardActivity extends BaseActivity {

    private CardView tasksCard, scheduleCard, gradesCard;
    private TextView welcomeText, userEmailText;
    
    public StudentDashboardActivity() {
        super("Dashboard");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_dashboard, R.id.student_dashboard_root);

        initializeViews();
        setupClickListeners();
        setupUserInfo();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        userEmailText = findViewById(R.id.user_email_text);
        tasksCard = findViewById(R.id.tasks_card);
        scheduleCard = findViewById(R.id.schedule_card);
        gradesCard = findViewById(R.id.grades_card);
    }

    private void setupClickListeners() {
        tasksCard.setOnClickListener(v -> openTasksActivity());
        scheduleCard.setOnClickListener(v -> openScheduleActivity());
        gradesCard.setOnClickListener(v -> openGradesActivity());
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

}
