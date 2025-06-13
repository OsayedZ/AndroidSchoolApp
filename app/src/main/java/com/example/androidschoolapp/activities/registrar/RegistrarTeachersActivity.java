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
import com.example.androidschoolapp.adapters.UsersAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.User;

import java.util.List;

public class RegistrarTeachersActivity extends BaseActivity {

    private RecyclerView teachers_recycler;

    private UsersAdapter usersAdapter;
    private Button btn_add;

    public RegistrarTeachersActivity() {
        super("Manage Teachers");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_teachers, R.id.registrar_teachers);

        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTeachers();
    }

    private void initializeViews() {
        teachers_recycler = findViewById(R.id.teachers_recycler);
        btn_add = findViewById(R.id.btn_add);
        teachers_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTeachers() {
        apiClient.getTeachers(new ApiClient.DataCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                usersAdapter = new UsersAdapter(RegistrarTeachersActivity.this, result);
                teachers_recycler.setAdapter(usersAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void handleEvents() {
        btn_add.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarTeachersActivity.this, RegistrarManageST.class);
            startActivity(intent);
        });
    }
}

