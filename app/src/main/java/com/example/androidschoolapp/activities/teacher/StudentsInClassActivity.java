package com.example.androidschoolapp.activities.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidschoolapp.R;
import com.example.androidschoolapp.adapters.ClassStudentsAdapter;
import com.example.androidschoolapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class StudentsInClassActivity extends AppCompatActivity {

    private TextView className;
    private TextView classId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_in_class);

        List<User> studentsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            studentsList.add(new User(i, ("one"+i), "email"+i));

        }

        className = findViewById(R.id.class_name);
        classId = findViewById(R.id.class_id);

        className.setText(getIntent().getStringExtra("CLASS_NAME"));
        classId.setText(getIntent().getStringExtra("CLASS_ID"));

        RecyclerView recyclerView = findViewById(R.id.students_in_class_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ClassStudentsAdapter(studentsList));
    }

}
