package com.example.androidschoolapp.activities.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.TasksForTeacherAdapter;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TeacherTasksActivity extends BaseActivity {

    private RecyclerView tasks_recycler_view;
    private Button addBtn;

    public TeacherTasksActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_tasks);


        initializeViews();

//        recyclerView.setAdapter(new TasksForTeacherAdapter(tasksList));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void initializeViews() {
        addBtn = findViewById(R.id.addBtn);
        tasks_recycler_view = findViewById(R.id.tasks_recycler_view);
        tasks_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTasks() {
//        apiClient.getTeacherTasks();
//        apiClient.getTasks();
    }

}
