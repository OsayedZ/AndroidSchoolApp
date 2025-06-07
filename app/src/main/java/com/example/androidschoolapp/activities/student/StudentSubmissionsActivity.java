package com.example.androidschoolapp.activities.student;

import android.os.Bundle;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class StudentSubmissionsActivity extends BaseActivity {

    public StudentSubmissionsActivity() {
        super("Task Submissions");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_submissions, R.id.student_submissions_root);
    }
}
