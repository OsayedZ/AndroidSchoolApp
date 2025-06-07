package com.example.androidschoolapp.activities.student;

import android.os.Bundle;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;

public class StudentGradesActivity extends BaseActivity {

    public StudentGradesActivity() {
        super("Grades");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_grades, R.id.student_grades_root);
    }
}
