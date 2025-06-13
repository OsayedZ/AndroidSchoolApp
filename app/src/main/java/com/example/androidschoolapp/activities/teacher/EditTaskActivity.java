package com.example.androidschoolapp.activities.teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    private EditText edtTaskName;
    private Spinner spnTaskType;
    private EditText edtTaskDecs;
    private Spinner spnTaskSubject;
    private Button btnEditTask;
    private TextView edtTaskNameError;
    private TextView spnTaskTypeError;
    private TextView edtTaskDecsError;
    private TextView spnTaskSubjectError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        List<Task> tasksList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasksList.add(new Task( ("one"+i), ""+i,""+i, ""+i));

        }

        setUpViews();
        setTypes();
        setSubjects();
        onClick(btnEditTask);

    }

    private void setUpViews(){
        edtTaskName = findViewById(R.id.edt_task_name);
        spnTaskType = findViewById(R.id.spn_task_type);
        edtTaskDecs = findViewById(R.id.edt_task_desc);
        spnTaskSubject = findViewById(R.id.spn_subjects);
        btnEditTask = findViewById(R.id.btn_edit_task);
        edtTaskNameError = findViewById(R.id.edt_task_name_error);
        spnTaskTypeError = findViewById(R.id.spn_task_type_error);
        edtTaskDecsError = findViewById(R.id.edt_task_desc_error);
        spnTaskSubjectError =findViewById(R.id.spn_subjects_error);
    }

    private void setTypes(){
        List<String> Types = new ArrayList<>();
        Types.add("");
        Types.add("Assignment");
        Types.add("Exam");

        ArrayAdapter<String> TypesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Types
        );

        TypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTaskType.setAdapter(TypesAdapter);
    }

    private void setSubjects(){
        List<String> subjects = new ArrayList<>();
        subjects.add("");
        subjects.add("Math");
        subjects.add("Science");
        subjects.add("History");
        subjects.add("English");
        subjects.add("Physics");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                subjects
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTaskSubject.setAdapter(adapter);
    }

    private void onClick(Button add){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeAllErrorsGone();

                List<Boolean> Errors = new ArrayList<>();

                Errors.add(edtTaskName.getText().toString().isEmpty());
                if (Errors.get(0)){
                    edtTaskNameError.setVisibility(View.VISIBLE);
                }

                Errors.add(spnTaskType.getSelectedItem().toString().isEmpty());
                if (Errors.get(1)){
                    spnTaskTypeError.setVisibility(View.VISIBLE);
                }

                Errors.add(edtTaskDecs.getText().toString().isEmpty());
                if (Errors.get(2)){
                    edtTaskDecsError.setVisibility(View.VISIBLE);
                }

                Errors.add(spnTaskSubject.getSelectedItem().toString().isEmpty());
                if (Errors.get(3)){
                    spnTaskSubjectError.setVisibility(View.VISIBLE);
                }

                if (checkErrors(Errors)){

                }
            }
        });
    }

    private void makeAllErrorsGone(){
        edtTaskNameError.setVisibility(View.GONE);
        spnTaskTypeError.setVisibility(View.GONE);
        edtTaskDecsError.setVisibility(View.GONE);
        spnTaskSubjectError.setVisibility(View.GONE);
    }

    private boolean checkErrors(List<Boolean> Errors){

        for (int i = 0; i < Errors.size(); i++) {
            if (Errors.get(i)){
                return false;
            }
        }
        return true;
    }
}
