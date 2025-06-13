package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.models.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherManageTask extends BaseActivity {

    private EditText edtTaskName;
    private Spinner spnTaskType;
    private EditText edtTaskDecs;
    private Spinner spnTaskSubject;
    private Button btnSubmit;

    private List<String> Types = Arrays.asList("Assignment", "Exam");

    private Task selected_task;

    public TeacherManageTask() {
        super("Manage Task");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_manage_task, R.id.teacher_manage_task);

        initializeViews();
        setTypes();
        setupPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSubjects();
    }

    private void initializeViews() {
        edtTaskName = findViewById(R.id.edt_task_name);
        spnTaskType = findViewById(R.id.spn_task_type);
        edtTaskDecs = findViewById(R.id.edt_task_desc);
        spnTaskSubject = findViewById(R.id.spn_subjects);
        btnSubmit = findViewById(R.id.btn_add_task);
    }

    private void setTypes(){
        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Types
        );

        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTaskType.setAdapter(TypesAdapter);
    }

    private void setSubjects() {
        apiClient.getSubjectsForTeacher(new ApiClient.DataCallback<List<Subject>>() {
            @Override
            public void onSuccess(List<Subject> result) {
                ArrayAdapter<Subject> subjectsAdapter = new ArrayAdapter<>(
                        TeacherManageTask.this,
                        android.R.layout.simple_spinner_item,
                        result
                );
                subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTaskSubject.setAdapter(subjectsAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void setupPage() {
        boolean mode = getIntent().getBooleanExtra("Edit", false);
        if(mode) {
            edtTaskName.setVisibility(View.GONE);
            spnTaskSubject.setVisibility(View.GONE);

            String t = getIntent().getStringExtra("Task");
            Gson gson = new Gson();

            Task task = gson.fromJson(t, Task.class);
            String type = task.getType();
            String description = task.getDescription();
            selected_task = task;

            edtTaskDecs.setText(description);

            for (int i = 0; i < Types.size(); i++) {
                if (Types.get(i).equalsIgnoreCase(type)) {
                    spnTaskType.setSelection(i);
                    break;
                }
            }

            btnSubmit.setText("Edit");
            btnSubmit.setOnClickListener(e -> editTask());
        } else {
            btnSubmit.setText("Add");
            btnSubmit.setOnClickListener(e -> addTask());
        }
    }

    private void addTask() {
        String task_name = edtTaskName.getText().toString();
        String task_description = edtTaskDecs.getText().toString();
        String type = spnTaskType.getSelectedItem().toString();
        int subjectID = ((Subject)spnTaskSubject.getSelectedItem()).getId();

        if(task_name.isEmpty() || task_description.isEmpty() || type.isEmpty() || subjectID == 0) {
            showToast("Please enter all fields");
            return;
        }

        Task task = new Task();
        task.setName(task_name);
        task.setDescription(task_description);
        task.setType(type);
        task.setSubjectId(subjectID);

        apiClient.addTask(task, new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Task Created Successfully!");
                finish();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void editTask() {
        String task_description = edtTaskDecs.getText().toString();
        String type = spnTaskType.getSelectedItem().toString();
        if(task_description.isEmpty() || type.isEmpty()) {
            showToast("Please enter all fields");
            return;
        }

        Task task = new Task();
        task.setName(selected_task.getName());
        task.setDescription(task_description);
        task.setType(type);
        task.setSubjectId(selected_task.getSubjectId());

        apiClient.editTask(task, new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Task Updated Successfully!");
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });

    }


}