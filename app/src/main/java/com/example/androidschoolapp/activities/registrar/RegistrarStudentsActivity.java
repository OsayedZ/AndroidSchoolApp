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
import com.example.androidschoolapp.adapters.SubjectsAdapter;
import com.example.androidschoolapp.adapters.UsersAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.User;

import java.util.List;

public class RegistrarStudentsActivity extends BaseActivity {

    private RecyclerView students_recycler;

    private UsersAdapter usersAdapter;
    private Button btn_add;

    public RegistrarStudentsActivity() {
        super("Manage Students");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_students, R.id.registrar_manage_students);
        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void initializeViews() {
        students_recycler = findViewById(R.id.students_recycler);
        btn_add = findViewById(R.id.btn_add);
        students_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadStudents() {
        apiClient.getStudents(new ApiClient.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                usersAdapter = new UsersAdapter(RegistrarStudentsActivity.this, result);
                students_recycler.setAdapter(usersAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void handleEvents() {
        btn_add.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarStudentsActivity.this, RegistrarManageST.class);
            startActivity(intent);
        });
    }

}