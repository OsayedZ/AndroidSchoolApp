package com.example.androidschoolapp.activities.student;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.GradesAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class StudentGradesActivity extends BaseActivity {

    private RecyclerView gradesRecyclerView;
    private GradesAdapter gradesAdapter;
    private TextView emptyView;
    private ProgressBar loadingProgress;
    private boolean isDataLoaded = false;

    public StudentGradesActivity() {
        super("Grades");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_student_grades, R.id.student_grades_root);

        // Initialize views
        initializeViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load grades
        loadGrades();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload grades when coming back to this activity
        if (isDataLoaded) {
            loadGrades();
        }
    }

    private void initializeViews() {
        gradesRecyclerView = findViewById(R.id.grades_recycler_view);
        emptyView = findViewById(R.id.empty_view);
        loadingProgress = findViewById(R.id.loading_progress);
    }

    private void setupRecyclerView() {
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gradesAdapter = new GradesAdapter(this, new ArrayList<>());
        gradesRecyclerView.setAdapter(gradesAdapter);
    }

    private void loadGrades() {
        showLoading(true);
        
        apiClient.getStudentTasks(new ApiClient.DataCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> tasks) {
                isDataLoaded = true;
                showLoading(false);
                
                // Filter tasks that have grades (mark > 0)
                List<Task> gradedTasks = filterGradedTasks(tasks);
                updateGradesUI(gradedTasks);
            }

            @Override
            public void onError(String errorMessage) {
                isDataLoaded = false;
                showLoading(false);
                showError(errorMessage);
            }
        });
    }

    private List<Task> filterGradedTasks(List<Task> allTasks) {
        List<Task> gradedTasks = new ArrayList<>();
        
        for (Task task : allTasks) {
            // Only include tasks that have been graded (mark > 0)
            if (task.getMark() > 0) {
                gradedTasks.add(task);
            }
        }
        
        return gradedTasks;
    }

    private void updateGradesUI(List<Task> gradedTasks) {
        if (gradedTasks.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);
            gradesAdapter.updateGrades(gradedTasks);
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            loadingProgress.setVisibility(View.VISIBLE);
            gradesRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        } else {
            loadingProgress.setVisibility(View.GONE);
        }
    }

    private void showEmptyState(boolean show) {
        if (show) {
            gradesRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No grades available yet.\nComplete some assignments to see your grades here.");
        } else {
            gradesRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showError(String errorMessage) {
        gradesRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText("Error loading grades: " + errorMessage);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
