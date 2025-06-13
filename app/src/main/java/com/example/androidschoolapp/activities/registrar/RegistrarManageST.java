package com.example.androidschoolapp.activities.registrar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistrarManageST extends BaseActivity {

    private TextView page_title;

    private EditText edt_name, edt_email, edt_age, edt_gender, edt_password;

    private Spinner spn_role;

    private Button btn_submit;

    private User selected_user;
    private List<String> roles = Arrays.asList("Student", "Teacher");

    public RegistrarManageST() {
        super("Manage User");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupContentView(R.layout.activity_registrar_manage_st, R.id.registrar_manage_st);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initializeViews() {
        page_title = findViewById(R.id.page_title);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_age = findViewById(R.id.edt_age);
        edt_gender = findViewById(R.id.edt_gender);
        edt_password = findViewById(R.id.edt_password);
        spn_role = findViewById(R.id.spn_role);
        btn_submit = findViewById(R.id.btn_submit);

        Spinner spnRole = findViewById(R.id.spn_role);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnRole.setAdapter(adapter);
    }

    private void addUser() {
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String a = edt_age.getText().toString();
        String gender = edt_gender.getText().toString();
        String selectedRole = spn_role.getSelectedItem().toString();
        String password = edt_password.getText().toString();

        if (name.isEmpty() || email.isEmpty() || a.isEmpty() || gender.isEmpty() || selectedRole.isEmpty() || password.isEmpty()) {
            showToast("Please Enter all fields");
            return;
        }

        int age = Integer.parseInt(a);
        boolean gen = false;

        if (gender.equalsIgnoreCase("male")) {
            gen = true;
        }

        Date now = new Date();

        apiClient.addStudentOrTeacher(new User(0, name, email, age, gen, selectedRole, password, now, 0), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("User added Successfully!");
                finish();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void editUser() {
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String a = edt_age.getText().toString();
        String gender = edt_gender.getText().toString();
        String selectedRole = spn_role.getSelectedItem().toString();
        String password = edt_password.getText().toString();

        if (name.isEmpty() || email.isEmpty() || a.isEmpty() || gender.isEmpty() || selectedRole.isEmpty()) {
            showToast("Please Enter all fields");
            return;
        }

        int age = Integer.parseInt(a);
        boolean gen = false;

        if (gender.equalsIgnoreCase("male")) {
            gen = true;
        }

        Date now = new Date();

        apiClient.editStudentOrTeacher(new User(selected_user.getId(), name, email, age, gen, selectedRole, password, now, selected_user.getClassId()), new ApiClient.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                finish();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


    }

    private void loadData() {
        boolean mode = getIntent().getBooleanExtra("Edit", false);
        if (mode) {
            btn_submit.setText("Edit");
            page_title.setText("Edit User");

            Gson gson = new Gson();
            selected_user = gson.fromJson(getIntent().getStringExtra("User"), User.class);

            edt_name.setText(selected_user.getName());
            edt_email.setText(selected_user.getEmail());
            edt_age.setText(String.valueOf(selected_user.getAge()));
            edt_gender.setText(selected_user.getGenderString());

            String userRole = selected_user.getRole();
            showToast(userRole);

            for (int i = 0; i < roles.size(); i++) {
                if (roles.get(i).equalsIgnoreCase(userRole)) {
                    spn_role.setSelection(i);
                    break;
                }
            }

            btn_submit.setOnClickListener(e -> {
                editUser();
            });

        } else {
            btn_submit.setText("Add");
            page_title.setText("Add User");

            btn_submit.setOnClickListener(e -> {
                addUser();
            });
        }
    }
}