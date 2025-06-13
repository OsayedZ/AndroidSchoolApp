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
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TeacherTasksActivity extends BaseActivity {

    private RecyclerView tasks_recycler_view;
    private TasksForTeacherAdapter adapter;
    private Button addBtn;

    public TeacherTasksActivity() {
        super("Tasks");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_tasks, R.id.student_tasks_act);

        initializeViews();
        handleEvents();
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

    private void handleEvents() {
        addBtn.setOnClickListener(e -> {
            Intent intent = new Intent(TeacherTasksActivity.this, TeacherManageTask.class);
            startActivity(intent);
        });
    }

    private void loadTasks() {
        apiClient.getTeacherTasks(new ApiClient.DataCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                 adapter = new TasksForTeacherAdapter(TeacherTasksActivity.this,result);
                 tasks_recycler_view.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

}
