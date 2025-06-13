package com.example.androidschoolapp.activities.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.models.Class;
import com.google.gson.Gson;

public class RegistrarManageClass extends BaseActivity {

    private Button btn_add;
    private EditText edt_name;

    private Gson gson = new Gson();
    private Class selected_class;
    public RegistrarManageClass() {
        super("Manage Class");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupContentView(R.layout.activity_registrar_manage_class, R.id.registrar_manage_class);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean m = getIntent().getBooleanExtra("Edit", false);
        if (m) {
            btn_add.setText("Edit");
            btn_add.setOnClickListener(e -> editClass());

            String json = getIntent().getStringExtra("Class");
            if (!json.isEmpty()) {
                Class c = gson.fromJson(json, Class.class);
                selected_class = c;
                edt_name.setText(c.getName());
            }
        } else {
            btn_add.setText("Add");
            btn_add.setOnClickListener(e -> addClass());
        }

    }

    private void initializeViews() {
        btn_add = findViewById(R.id.btn_add);
        edt_name = findViewById(R.id.edt_name);
    }

    private void addClass() {
        String className = edt_name.getText().toString();

        if (className.isEmpty()) {
            showToast("Please Enter a class name");
            return;
        }

        apiClient.addClass(new Class(0, className), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Class added Successfully!");
            }

            @Override
            public void onError(String errorMessage) {
                showToast(errorMessage);
            }
        });
    }

    private void editClass() {
        String className = edt_name.getText().toString();

        if (className.isEmpty()) {
            showToast("Please Enter a class name");
            return;
        }

        apiClient.editClass(new Class(selected_class.getId(), className), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("Class Updated Successfully!");
                Intent intent = new Intent(RegistrarManageClass.this, RegistrarClassesActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
}