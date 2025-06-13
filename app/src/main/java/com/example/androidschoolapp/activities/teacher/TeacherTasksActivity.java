package com.example.androidschoolapp.activities.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.adapters.TasksForTeacherAdapter;
import com.example.androidschoolapp.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TeacherTasksActivity extends AppCompatActivity {

    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_tasks);

        List<Task> tasksList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasksList.add(new Task(("one" + i), "" + i, "" + i, "" + i));

        }

        addBtn = findViewById(R.id.addBtn);

        onClick(addBtn);

        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TasksForTeacherAdapter(tasksList));
    }

    private void onClick(Button add) {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CreateTaskActivity.class);


                v.getContext().startActivity(intent);
            }
        });
    }
}
