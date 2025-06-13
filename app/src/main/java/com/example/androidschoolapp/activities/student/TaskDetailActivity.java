package com.example.androidschoolapp.activities.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TaskDetailActivity extends BaseActivity {

    private TextView taskTitleTextView;
    private TextView taskTypeTextView;
    private TextView taskDescriptionTextView;
    private TextView taskStatusTextView;
    private LinearLayout submittedAnswerContainer;
    private TextView submittedAnswerText;
    private TextInputLayout answerInputLayout;
    private TextInputEditText answerEditText;
    private MaterialButton submitButton;

    private String taskId;
    private boolean isTaskSubmitted = false;

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
        String existingAnswer = getIntent().getStringExtra("task_answer");

        // Initialize views
        initializeViews();

        // Set task data
        setupTaskData(taskTitle, taskDescription, taskType, existingAnswer);

        // Set submit button click listener
        submitButton.setOnClickListener(v -> submitTaskAnswer());
    }

    private void initializeViews() {
        taskTitleTextView = findViewById(R.id.task_title);
        taskTypeTextView = findViewById(R.id.task_type);
        taskDescriptionTextView = findViewById(R.id.task_description);
        taskStatusTextView = findViewById(R.id.task_status);
        submittedAnswerContainer = findViewById(R.id.submitted_answer_container);
        submittedAnswerText = findViewById(R.id.submitted_answer_text);
        answerInputLayout = findViewById(R.id.answer_input_layout);
        answerEditText = findViewById(R.id.answer_input);
        submitButton = findViewById(R.id.submit_button);
    }

    private void setupTaskData(String taskTitle, String taskDescription, String taskType, String existingAnswer) {
        taskTitleTextView.setText(taskTitle);
        taskTypeTextView.setText(taskType);
        taskDescriptionTextView.setText(taskDescription);

        // Check if task is already submitted
        isTaskSubmitted = existingAnswer != null && !existingAnswer.trim().isEmpty();
        
        if (isTaskSubmitted) {
            showSubmittedState(existingAnswer);
        } else {
            showNotSubmittedState();
        }
    }

    private void showSubmittedState(String submittedAnswer) {
        // Update status
        taskStatusTextView.setText("Status: Submitted");
        taskStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.success));
        taskStatusTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_check_circle, 0, 0, 0);

        // Show submitted answer
        submittedAnswerContainer.setVisibility(View.VISIBLE);
        submittedAnswerText.setText(submittedAnswer);

        // Hide input fields and submit button
        answerInputLayout.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
    }

    private void showNotSubmittedState() {
        // Update status
        taskStatusTextView.setText("Status: Not Submitted");
        taskStatusTextView.setTextColor(ContextCompat.getColor(this, R.color.warning));
        taskStatusTextView.setCompoundDrawablesWithIntrinsicBounds(
            android.R.drawable.ic_dialog_info, 0, 0, 0);

        // Hide submitted answer container
        submittedAnswerContainer.setVisibility(View.GONE);

        // Show input fields and submit button
        answerInputLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
    }

    private void submitTaskAnswer() {
        // Don't allow submission if already submitted
        if (isTaskSubmitted) {
            Toast.makeText(this, "This task has already been submitted", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String answer = answerEditText.getText().toString().trim();
        
        if (answer.isEmpty()) {
            answerInputLayout.setError("Answer cannot be empty");
            return;
        }
        
        if (answer.length() < 10) {
            answerInputLayout.setError("Please provide a more detailed answer (minimum 10 characters)");
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
                Toast.makeText(TaskDetailActivity.this, "Answer submitted successfully!", Toast.LENGTH_LONG).show();
                
                // Update UI to show submitted state
                isTaskSubmitted = true;
                showSubmittedState(answer);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(TaskDetailActivity.this, "Failed to submit: " + errorMessage, Toast.LENGTH_LONG).show();
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