package com.example.androidschoolapp.activities.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.androidschoolapp.adapters.SubjectsAdapter;
import com.example.androidschoolapp.adapters.SubjectsClassAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.ClassSubject;
import com.example.androidschoolapp.models.Subject;
import com.example.androidschoolapp.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RegistrarClass extends BaseActivity {

    private Gson gson = new Gson();

    private TextView class_name;

    private Spinner spn_subjects;

    private Button btn_assign;

    private Class aClass;

    private RecyclerView subjects_class_recycler;

    private SubjectsClassAdapter subjectsClassAdapter;

    List<Subject> assignedSubjects = new ArrayList<>();

    public RegistrarClass() {
        super("Class");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_class, R.id.registrar_class);

        initializeViews();
        loadData();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
        loadAssignedSubjects();
    }

    private void initializeViews() {
        class_name = findViewById(R.id.class_name);
        spn_subjects = findViewById(R.id.spn_subjects);
        subjects_class_recycler = findViewById(R.id.subjects_class_recycler);
        subjects_class_recycler.setLayoutManager(new LinearLayoutManager(this));
        btn_assign = findViewById(R.id.btn_assign);
    }

    private void loadData() {
        String c = getIntent().getStringExtra("Class");
        aClass = gson.fromJson(c, Class.class);
        class_name.setText(aClass.getName());
    }

    private void loadSubjects() {
        apiClient.getSubjects(new ApiClient.DataCallback<List<com.example.androidschoolapp.models.Subject>>() {
            @Override
            public void onSuccess(List<Subject> result) {
                ArrayAdapter<Subject> adapter = new ArrayAdapter<>(
                        RegistrarClass.this,
                        android.R.layout.simple_spinner_item,
                        result
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_subjects.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void loadAssignedSubjects() {
        apiClient.getSubjectsForClass(aClass.getId(), new ApiClient.DataCallback<ClassSubject>() {
            @Override
            public void onSuccess(ClassSubject result) {
                subjectsClassAdapter = new SubjectsClassAdapter(RegistrarClass.this, result);
                subjects_class_recycler.setAdapter(subjectsClassAdapter);
                assignedSubjects = result.getSubjects();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void handleEvents() {
        btn_assign.setOnClickListener(e -> {
            int classID = aClass.getId();
            Subject selected_subject = (Subject) spn_subjects.getSelectedItem();
            int subjectID = selected_subject.getId();

            apiClient.assignSubjectToClass(subjectID, classID, new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    showToast("Subject Assigned Successfully!");
                    assignedSubjects.add(selected_subject);

                    subjectsClassAdapter.notifyItemInserted(assignedSubjects.size() - 1);
                    subjects_class_recycler.scrollToPosition(assignedSubjects.size() - 1);
                }

                @Override
                public void onError(String errorMessage) {
                    showToast(errorMessage);
                }
            });
        });
    }
}