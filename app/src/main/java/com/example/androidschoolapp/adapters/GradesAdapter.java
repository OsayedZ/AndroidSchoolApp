package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.Task;

import java.text.DecimalFormat;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradeViewHolder> {

    private List<Task> gradedTasks;
    private Context context;

    public GradesAdapter(Context context, List<Task> gradedTasks) {
        this.context = context;
        this.gradedTasks = gradedTasks;
    }

    public void updateGrades(List<Task> newGradedTasks) {
        this.gradedTasks = newGradedTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Task task = gradedTasks.get(position);
        
        // Set task name
        holder.taskNameTextView.setText(task.getName());
        
        // Set task type
        holder.taskTypeTextView.setText(task.getType());
        
        // Set task description
        holder.taskDescriptionTextView.setText(task.getDescription());
        
        // Set and format grade mark
        double mark = task.getMark();
        if (mark > 0) {
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedMark = df.format(mark);
            
            holder.gradeMarkTextView.setText(formattedMark + "%");
            
            
            // Set grade color based on performance
            int gradeColor = getGradeColor(mark);
            GradientDrawable gradeBackground = new GradientDrawable();
            gradeBackground.setShape(GradientDrawable.RECTANGLE);
            gradeBackground.setColor(ContextCompat.getColor(context, gradeColor));
            gradeBackground.setCornerRadius(20f);
            holder.gradeMarkTextView.setBackground(gradeBackground);
        } else {
            holder.gradeMarkTextView.setText("N/A");
            holder.gradeMarkTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.text_hint));
        }
        
        // Set subject label
        holder.subjectLabelTextView.setText("Subject ID: " + task.getSubjectId());
        
        // Set status
        holder.statusTextView.setText("Graded");
        holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.success));
    }

    @Override
    public int getItemCount() {
        return gradedTasks.size();
    }

    private int getGradeColor(double mark) {
        // Assuming mark is out of 100
        double percentage = mark > 100 ? (mark / 100) * 100 : mark;
        
        if (percentage >= 90) {
            return R.color.success; // Green for excellent
        } else if (percentage >= 80) {
            return R.color.primary_blue; // Blue for good
        } else if (percentage >= 70) {
            return R.color.warning; // Orange for satisfactory
        } else {
            return android.R.color.holo_red_light; // Red for needs improvement
        }
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        TextView taskTypeTextView;
        TextView taskDescriptionTextView;
        TextView gradeMarkTextView;
        TextView subjectLabelTextView;
        TextView statusTextView;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.grade_task_name);
            taskTypeTextView = itemView.findViewById(R.id.grade_task_type);
            taskDescriptionTextView = itemView.findViewById(R.id.grade_task_description);
            gradeMarkTextView = itemView.findViewById(R.id.grade_mark);
            subjectLabelTextView = itemView.findViewById(R.id.grade_subject_label);
            statusTextView = itemView.findViewById(R.id.grade_status);
        }
    }
} 