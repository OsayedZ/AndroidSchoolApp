package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.teacher.TeacherGradeTask;
import com.example.androidschoolapp.activities.teacher.TeacherManageTask;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;
import com.google.gson.Gson;

import java.util.List;

public class TasksForTeacherAdapter extends RecyclerView.Adapter<TasksForTeacherAdapter.TaskViewHolder>{


    private Context context;
    private List<Task> tasks;

    private ApiClient apiClient;

    public TasksForTeacherAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
        this.apiClient = ApiClient.getInstance(context);
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

        holder.taskNameTextView.setText("Task: " + currentTask.getName());
        holder.taskDescTextView.setText("Description: " + currentTask.getDescription());
        holder.taskTypeTextView.setText("Type: " + currentTask.getType());
        holder.taskDueDateTextView.setText("");


        holder.task_card.setOnClickListener(e -> {
            Intent intent = new Intent(context, TeacherGradeTask.class);
            intent.putExtra("task_name", currentTask.getName());
            intent.putExtra("subject_id", currentTask.getSubjectId());
            context.startActivity(intent);
        });

        holder.editBtn.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), TeacherManageTask.class);
            Gson gson = new Gson();

            intent.putExtra("Edit", true);
            intent.putExtra("Task", gson.toJson(currentTask));
            context.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener( v -> {
            Task task = new Task();
            task.setSubjectId(currentTask.getSubjectId());
            task.setName(currentTask.getName());

            apiClient.deleteTask(task, new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        tasks.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        LinearLayout task_card;
        TextView taskNameTextView;
        TextView taskDescTextView;
        TextView taskTypeTextView;
        TextView taskDueDateTextView;
        Button editBtn;
        Button deleteBtn;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            task_card = itemView.findViewById(R.id.task_card);
            taskNameTextView = itemView.findViewById(R.id.task_name);
            taskDescTextView = itemView.findViewById(R.id.task_description);
            taskTypeTextView = itemView.findViewById(R.id.task_type);
            taskDueDateTextView = itemView.findViewById(R.id.task_due_date);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
