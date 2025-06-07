package com.example.androidschoolapp.activities.student;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.TasksAdapter;
import com.example.androidschoolapp.api.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentTasksActivity extends BaseActivity {

    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksAdapter;
    private TextView emptyView;

    public StudentTasksActivity() {
        super("Tasks");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_tasks, R.id.student_tasks_root);

        // Initialize views
        tasksRecyclerView = findViewById(R.id.tasks_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        // Setup RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TasksAdapter(new ArrayList<>());
        tasksRecyclerView.setAdapter(tasksAdapter);

        // Load tasks
        loadTasks();
    }

    private void loadTasks() {

        apiClient.getStudentTasks(
                new ApiClient.DataCallback<List<Map<String, String>>>() {
                    @Override
                    public void onSuccess(List<Map<String, String>> tasks) {
                        // Update UI with tasks
                        if (tasks.isEmpty()) {
                            tasksRecyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            tasksRecyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                            tasksAdapter.updateTasks(tasks);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Show empty state with error
                        tasksRecyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText("Error loading tasks: " + errorMessage);
                    }
                }
        );
    }


}
