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
import com.example.androidschoolapp.adapters.ClassSubjectsAdapter;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.ClassSubject;

public class SubjectsOfClassActivity extends BaseActivity {

    private TextView class_name_view;
    private RecyclerView subjects_in_class;

    private ClassSubjectsAdapter adapter;

    public SubjectsOfClassActivity() {
        super("Class");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_subjects_of_class, R.id.subjects_in_class_act);


        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }

    private void initializeViews() {
        class_name_view = findViewById(R.id.class_name);
        subjects_in_class = findViewById(R.id.subjects_in_class);
        subjects_in_class.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {

        String class_id = getIntent().getStringExtra("CLASS_ID");
        String class_name = getIntent().getStringExtra("CLASS_NAME");
        class_name_view.setText(class_name);

        apiClient.getSubjectsForClass(Integer.parseInt(class_id), new ApiClient.DataCallback<ClassSubject>() {
            @Override
            public void onSuccess(ClassSubject result) {
                adapter = new ClassSubjectsAdapter(result.getSubjects());
                subjects_in_class.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }

}