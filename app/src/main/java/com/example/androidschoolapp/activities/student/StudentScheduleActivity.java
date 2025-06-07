package com.example.androidschoolapp.activities.student;

import android.os.Bundle;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class StudentScheduleActivity extends BaseActivity {

    public StudentScheduleActivity() {
        super("Schedule");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_schedule, R.id.student_schedule_root);
    }
}
