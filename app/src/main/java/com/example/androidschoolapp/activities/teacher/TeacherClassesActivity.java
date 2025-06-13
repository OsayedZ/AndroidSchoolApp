package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.activities.registrar.RegistrarSubjectsActivity;
import com.example.androidschoolapp.adapters.ClassAdapter;
import com.example.androidschoolapp.adapters.SubjectsAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;

import java.util.List;

public class TeacherClassesActivity extends BaseActivity {

    public TeacherClassesActivity() {
        super("Classes");
    }

    private RecyclerView teacher_classes;

    private ClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_teacher_classes, R.id.teacher_classes_act);
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    private void initializeViews() {
        teacher_classes = findViewById(R.id.teacher_classes);
        teacher_classes.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadClasses() {
        apiClient.getTeacherClasses(new ApiClient.DataCallback<List<Class>>() {
            @Override
            public void onSuccess(List<Class> result) {
                adapter = new ClassAdapter(result);
                teacher_classes.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}