package com.example.androidschoolapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;

import java.util.List;
import java.util.Map;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Map<String, String>> tasks;
    private OnTaskClickListener onTaskClickListener;

    public interface OnTaskClickListener {
        void onTaskClick(Map<String, String> task);
    }

    public TasksAdapter(List<Map<String, String>> tasks) {
        this.tasks = tasks;
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.onTaskClickListener = listener;
    }

    public void updateTasks(List<Map<String, String>> newTasks) {
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
        Map<String, String> task = tasks.get(position);
        
        holder.taskNameTextView.setText(task.get("Name"));
        holder.taskTypeTextView.setText(task.get("Type"));
        holder.taskDescriptionTextView.setText(task.get("Description"));
        
        // Format due date
        String dueDate = task.get("DueDate");
        if (dueDate != null && !dueDate.isEmpty()) {
            holder.taskDueDateTextView.setText("Due: " + dueDate);
            holder.taskDueDateTextView.setVisibility(View.VISIBLE);
        } else {
            holder.taskDueDateTextView.setVisibility(View.GONE);
        }
        
        // Set status if available
        String status = task.get("Status");
        if (status != null && !status.isEmpty()) {
            holder.taskStatusTextView.setText("Status: " + status);
            holder.taskStatusTextView.setVisibility(View.VISIBLE);
        } else {
            holder.taskStatusTextView.setVisibility(View.GONE);
        }

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