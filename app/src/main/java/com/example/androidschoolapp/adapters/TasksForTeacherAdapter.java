package com.example.androidschoolapp.adapters;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.teacher.EditTaskActivity;
import com.example.androidschoolapp.models.Task;

import java.util.List;

public class TasksForTeacherAdapter extends RecyclerView.Adapter<TasksForTeacherAdapter.TaskViewHolder>{


    private List<Task> tasks;


    public TasksForTeacherAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        Log.d(TAG, "onBindViewHolder: " +currentTask.getName());
        holder.taskNameTextView.append(currentTask.getName());
        holder.taskDescTextView.append(currentTask.getDescription());
        holder.taskTypeTextView.append(currentTask.getType());
//        holder.taskDueDateTextView.append(currentTask.getDueDate());



        holder.editBtn.setOnClickListener( v -> {
            Log.d(TAG, "Edit: " + currentTask.getName());

            Intent intent = new Intent(v.getContext(), EditTaskActivity.class);


            v.getContext().startActivity(intent);

        });

        holder.deleteBtn.setOnClickListener( v -> {
            Log.d(TAG, "Delete: " + currentTask.getName());

//            Intent intent = new Intent(v.getContext(), SubjectsOfTaskActivity.class);

//            intent.putExtra("CLASS_NAME", currentTask.getName());
//            intent.putExtra("CLASS_ID", String.valueOf(currentTask.getID()));
//
//            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size(); // Fixed: Return the actual size of the list
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        TextView taskDescTextView;
        TextView taskTypeTextView;
        TextView taskDueDateTextView;
        Button editBtn;
        Button deleteBtn;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.task_name);
            taskDescTextView = itemView.findViewById(R.id.task_description);
            taskTypeTextView = itemView.findViewById(R.id.task_type);
            taskDueDateTextView = itemView.findViewById(R.id.task_due_date);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
