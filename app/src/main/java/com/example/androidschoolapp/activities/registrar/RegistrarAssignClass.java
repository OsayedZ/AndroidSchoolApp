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

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.User;

import java.util.List;

public class RegistrarAssignClass extends BaseActivity {

    private Spinner spn_classes;
    private Button btn_assign;

    private int studentID;

    public RegistrarAssignClass() {
        super("Assign Student to Class");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_assign_class, R.id.registrar_assign_class);
        initializeViews();
        handleEvents();
        String i = getIntent().getStringExtra("StudentID");
        if(!i.isEmpty()) {
            studentID = Integer.parseInt(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    private void initializeViews() {
        spn_classes = findViewById(R.id.spn_classes);
        btn_assign = findViewById(R.id.btn_assign);
    }

    private void loadClasses() {
        apiClient.getClasses(new ApiClient.DataCallback<List<Class>>() {
            @Override
            public void onSuccess(List<Class> result) {
                ArrayAdapter<Class> adapter = new ArrayAdapter<>(
                        RegistrarAssignClass.this,
                        android.R.layout.simple_spinner_item,
                        result
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_classes.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void handleEvents() {
        btn_assign.setOnClickListener(e -> {
            Class selected = (Class) spn_classes.getSelectedItem();
            apiClient.setStudentClass(studentID, selected.getId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    showToast("Student Assigned to class Successfully!");
                    finish();
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        });
    }

}