package com.example.androidschoolapp.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.TasksAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class StudentTasksActivity extends BaseActivity {

    private RecyclerView tasksRecyclerView;
    private TasksAdapter tasksAdapter;
    private TextView emptyView;
    private boolean isDataLoaded = false;

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

        // Setup task item click listener
        tasksAdapter.setOnTaskClickListener(task -> {
            int taskId = task.getId();
            String taskTitle = task.getName();
            String taskDescription = task.getDescription();
            String taskType = task.getType();
            
            if (taskTitle != null) {
                Log.d("Test", "test");

                navigateToTaskDetail(String.valueOf(taskId), taskTitle, taskDescription, taskType);
            }
        });

        // Load tasks
        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks when coming back to this activity
        if (isDataLoaded) {
            loadTasks();
        }
    }

    private void navigateToTaskDetail(String taskId, String taskTitle, String taskDescription, 
                                     String taskType) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("task_id", taskId);
        intent.putExtra("task_title", taskTitle);
        intent.putExtra("task_description", taskDescription);
        intent.putExtra("task_type", taskType);
        startActivity(intent);
    }

    private void loadTasks() {
        // Show loading indicator if needed
        // You could show a ProgressBar here if desired

        apiClient.getStudentTasks(
                new ApiClient.DataCallback<List<Task>>() {
                    @Override
                    public void onSuccess(List<Task> tasks) {
                        isDataLoaded = true;
                        updateTasksUI(tasks);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        isDataLoaded = false;
                        showError(errorMessage);
                    }
                }
        );
    }

    private void updateTasksUI(List<Task> tasks) {
        if (tasks.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
            tasksAdapter.updateTasks(tasks);
        }
    }

    private void showEmptyState(boolean show) {
        if (show) {
            tasksRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No tasks available");
        } else {
            tasksRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showError(String errorMessage) {
        tasksRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText("Error loading tasks: " + errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
