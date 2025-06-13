package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.StudentsTaskAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;

import java.util.List;

public class TeacherGradeTask extends BaseActivity {


    private TextView task_name;

    private StudentsTaskAdapter adapter;

    private RecyclerView students_task_recycler;

    private int subjectID;

    public TeacherGradeTask() {
        super("Grade Tasks");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_grade_task, R.id.teacher_grade_task_act);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initializeViews() {
        task_name = findViewById(R.id.task_name);
        students_task_recycler = findViewById(R.id.students_task_recycler);
        students_task_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        String name = getIntent().getStringExtra("task_name");
        subjectID = getIntent().getIntExtra("subject_id", 0);

        task_name.setText("Task: " + name);

        apiClient.getStudentsForTask(name, subjectID, new ApiClient.DataCallback<List<Task>>() {
            @Override
            public void onSuccess(List<Task> result) {
                adapter = new StudentsTaskAdapter(TeacherGradeTask.this, result);
                students_task_recycler.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }
}