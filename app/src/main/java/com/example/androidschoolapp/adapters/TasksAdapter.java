package com.example.androidschoolapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskClickListener onTaskClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public TasksAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.onTaskClickListener = listener;
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        
        holder.taskNameTextView.setText(task.getName());
        holder.taskTypeTextView.setText(task.getType());
        holder.taskDescriptionTextView.setText(task.getDescription());
        
        // Due date not available in the current Task model
        holder.taskDueDateTextView.setVisibility(View.GONE);
        
        // Hide status view - not needed for student tasks
        holder.taskStatusTextView.setVisibility(View.GONE);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (onTaskClickListener != null) {
                onTaskClickListener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        TextView taskTypeTextView;
        TextView taskDescriptionTextView;
        TextView taskDueDateTextView;
        TextView taskStatusTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.task_name);
            taskTypeTextView = itemView.findViewById(R.id.task_type);
            taskDescriptionTextView = itemView.findViewById(R.id.task_description);
            taskDueDateTextView = itemView.findViewById(R.id.task_due_date);
            taskStatusTextView = itemView.findViewById(R.id.task_status);
        }
    }
} 