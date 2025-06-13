package com.example.androidschoolapp.activities.registrar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.models.User;
import com.google.gson.Gson;

import java.util.List;

public class RegistrarAssignTeacher extends BaseActivity {

    private Spinner spn_teachers;
    private Button btn_assign;
    private Subject subject;

    public RegistrarAssignTeacher() {
        super("Assign Teacher");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_assign_teacher, R.id.assign_teacher);
        String ssubject = getIntent().getStringExtra("Subject");

        Gson gson = new Gson();
        if(!ssubject.isEmpty()) {
            subject = gson.fromJson(ssubject, Subject.class);
        }

        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTeachers();
    }

    private void initializeViews() {
        spn_teachers = findViewById(R.id.spn_teachers);
        btn_assign = findViewById(R.id.btn_assign);
    }

    private void loadTeachers() {
        apiClient.getTeachers(new ApiClient.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                ArrayAdapter<User> adapter = new ArrayAdapter<>(
                        RegistrarAssignTeacher.this,
                        android.R.layout.simple_spinner_item,
                        result
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_teachers.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

    private void handleEvents() {
        btn_assign.setOnClickListener(e -> {
            User teacher = (User)spn_teachers.getSelectedItem();
            apiClient.assignTeacherToSubject(teacher.getId(), subject.getId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    showToast("Subject Assigned to teacher successfully!");
                }

                @Override
                public void onError(String errorMessage) {
                    showToast(errorMessage);
                }
            });
        });
    }
}