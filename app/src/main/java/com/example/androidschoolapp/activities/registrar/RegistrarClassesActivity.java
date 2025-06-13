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
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;

import java.util.List;

public class RegistrarClassesActivity extends BaseActivity {

    private RecyclerView classes_recycler;
    private Button btn_add;
    private ClassesAdapter classesAdapter;

    public RegistrarClassesActivity() {
        super("Manage Classes");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_classes, R.id.registrar_manage_classes);

        initializeViews();
        handleEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void initializeViews() {
        classes_recycler = findViewById(R.id.classes_recycler);
        btn_add = findViewById(R.id.btn_add);
        classes_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void handleEvents() {
        btn_add.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarClassesActivity.this, RegistrarManageClass.class);
            startActivity(intent);
        });
    }

    private void load() {
        apiClient.getClasses(new ApiClient.DataCallback<List<Class>>() {
            @Override
            public void onSuccess(List<Class> result) {
                classesAdapter = new ClassesAdapter(RegistrarClassesActivity.this, result);
                classes_recycler.setAdapter(classesAdapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

}