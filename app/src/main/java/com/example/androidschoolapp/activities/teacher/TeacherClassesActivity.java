package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidschoolapp.R;
import com.example.androidschoolapp.adapters.ClassAdapter;
import com.example.androidschoolapp.models.Class;

import java.util.ArrayList;
import java.util.List;

public class TeacherClassesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_classes);

        List<Class> classesList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            classesList.add(new Class(i, ("one"+i), ""+i,""+1));

        }

        RecyclerView recyclerView = findViewById(R.id.classes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ClassAdapter(classesList));
    }


}
