package com.example.androidschoolapp.activities.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.adapters.ClassesAdapter;
import com.example.androidschoolapp.adapters.SubjectsAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.Subject;

import java.util.List;

public class RegistrarSubjectsActivity extends BaseActivity {

    private RecyclerView subjects_recycler;
    private Button btn_add;
    private SubjectsAdapter subjectsAdapter;

    public RegistrarSubjectsActivity() {
        super("Subjects");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_subjects, R.id.registrar_manage_subjects);

        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void initializeViews() {
        subjects_recycler = findViewById(R.id.subjects_recycler);
        btn_add = findViewById(R.id.btn_add);
        subjects_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void handleEvents() {
        btn_add.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarSubjectsActivity.this, RegistrarManageSubject.class);
            startActivity(intent);
        });
    }

    private void load() {
        apiClient.getSubjects(new ApiClient.DataCallback<List<com.example.androidschoolapp.models.Subject>>() {
            @Override
            public void onSuccess(List<Subject> result) {
                subjectsAdapter = new SubjectsAdapter(RegistrarSubjectsActivity.this, result);
                subjects_recycler.setAdapter(subjectsAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }




}