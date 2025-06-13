package com.example.androidschoolapp.activities.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TaskDetailActivity extends BaseActivity {

    private TextView taskTitleTextView;
    private TextView taskTypeTextView;
    private TextView taskDescriptionTextView;
    private TextInputLayout answerInputLayout;
    private TextInputEditText answerEditText;
    private Button submitButton;

    private String taskId;

    public TaskDetailActivity() {
        super("Task Details");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_task_detail, R.id.task_detail_root);

        // Get task data from intent
        taskId = getIntent().getStringExtra("task_id");
        String taskTitle = getIntent().getStringExtra("task_title");
        String taskDescription = getIntent().getStringExtra("task_description");
        String taskType = getIntent().getStringExtra("task_type");

        // Initialize views
        taskTitleTextView = findViewById(R.id.task_title);
        taskTypeTextView = findViewById(R.id.task_type);
        taskDescriptionTextView = findViewById(R.id.task_description);
        answerInputLayout = findViewById(R.id.answer_input_layout);
        answerEditText = findViewById(R.id.answer_input);
        submitButton = findViewById(R.id.submit_button);

        // Set task data
        taskTitleTextView.setText(taskTitle);
        taskTypeTextView.setText(taskType);
        taskDescriptionTextView.setText(taskDescription);
        
        // Hide the status text view
        TextView taskStatusTextView = findViewById(R.id.task_status);
        if (taskStatusTextView != null) {
            taskStatusTextView.setVisibility(View.GONE);
        }

        // Set submit button click listener
        submitButton.setOnClickListener(v -> submitTaskAnswer());
    }

    private void submitTaskAnswer() {
        String answer = answerEditText.getText().toString().trim();
        
        if (answer.isEmpty()) {
            answerInputLayout.setError("Answer cannot be empty");
            return;
        }
        
        // Clear any previous errors
        answerInputLayout.setError(null);
        
        // Show loading
        showLoading(true);
        
        Task task = new Task();
        task.setId(Integer.parseInt(taskId));
        task.setAnswer(answer);
        
        // Call API to submit task
        apiClient.submitTask(task, new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String message) {
                showLoading(false);
                Toast.makeText(TaskDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                // Return to previous screen after a delay
                answerEditText.postDelayed(() -> finish(), 1500);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(TaskDetailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            submitButton.setEnabled(false);
            submitButton.setText("Submitting...");
        } else {
            submitButton.setEnabled(true);
            submitButton.setText("Submit Answer");
        }
    }
} 