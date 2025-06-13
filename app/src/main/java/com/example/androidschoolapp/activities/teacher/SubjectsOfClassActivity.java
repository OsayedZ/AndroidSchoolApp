package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.adapters.ClassSubjectsAdapter;
import com.example.androidschoolapp.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsOfClassActivity extends AppCompatActivity {

    private TextView className;
    private TextView classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_subjects);

        List<Subject> subjectList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            subjectList.add(new Subject(i, ("one"+i), i,"start"+i,"end"+i));

        }

        className = findViewById(R.id.class_name);
        classId = findViewById(R.id.class_id);

        className.setText(getIntent().getStringExtra("CLASS_NAME"));
        classId.setText(getIntent().getStringExtra("CLASS_ID"));

        RecyclerView recyclerView = findViewById(R.id.subjects_for_class_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ClassSubjectsAdapter(subjectList));
    }


}
