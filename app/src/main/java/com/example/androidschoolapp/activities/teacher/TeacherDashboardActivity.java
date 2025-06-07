package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class TeacherDashboardActivity extends BaseActivity {

    private CardView classesCard, tasksCard, scheduleCard;
    private TextView welcomeText;
    
    public TeacherDashboardActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_dashboard, R.id.teacher_dashboard_root);

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
