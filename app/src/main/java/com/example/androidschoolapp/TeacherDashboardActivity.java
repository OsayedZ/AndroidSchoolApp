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

public class TeacherDashboardActivity extends AppCompatActivity {

    private CardView classesCard, tasksCard, scheduleCard;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.teacher_dashboard_root), (v, insets) -> {
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
        classesCard = findViewById(R.id.classes_card);
        tasksCard = findViewById(R.id.tasks_card);
        scheduleCard = findViewById(R.id.schedule_card);
    }

    private void setupClickListeners() {
        classesCard.setOnClickListener(v -> openClassesActivity());
        tasksCard.setOnClickListener(v -> openTasksActivity());
        scheduleCard.setOnClickListener(v -> openScheduleActivity());
    }

    private void openClassesActivity() {
//        Intent intent = new Intent(TeacherDashboardActivity.this, TeacherClassesActivity.class);
//        startActivity(intent);
    }

    private void openTasksActivity() {
//        Intent intent = new Intent(TeacherDashboardActivity.this, TeacherTasksActivity.class);
//        startActivity(intent);
    }

    private void openScheduleActivity() {
//        Intent intent = new Intent(TeacherDashboardActivity.this, TeacherScheduleActivity.class);
//        startActivity(intent);
    }
}
