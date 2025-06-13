package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Task;

import java.util.List;

public class StudentsTaskAdapter extends RecyclerView.Adapter<StudentsTaskAdapter.StudentViewHolder> {

    private Context context;
    private List<Task> taskList;

    private ApiClient apiClient;
    public StudentsTaskAdapter(Context context,List<Task> taskList) {
        this.taskList = taskList;
        this.context = context;
        this.apiClient = ApiClient.getInstance(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_task_card, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.nameTextView.setText("Name: " + task.getStudentName());
        holder.emailTextView.setText("Email: " + task.getStudentEmail());
        holder.answerTextView.setText("Answer: " + task.getAnswer());

        if (task.getMark() != 0) {
            holder.markEditText.setText(String.valueOf(task.getMark()));
        }

        holder.btn_grade.setOnClickListener(e -> {

            if(holder.markEditText.getText().toString().isEmpty()) {
                return;
            }

            double mark = Double.parseDouble(holder.markEditText.getText().toString());
            Task task1 = new Task();
            task1.setMark(mark);

            task1.setUserId(task.getUserId());
            task1.setName(task.getName());
            task1.setSubjectId(task.getSubjectId());

            apiClient.gradeTask(task1, task.getUserId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    taskList.get(position).setMark(mark);
                    notifyItemChanged(position);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, answerTextView;
        EditText markEditText;

        Button btn_grade;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvStudentName);
            emailTextView = itemView.findViewById(R.id.tvStudentEmail);
            answerTextView = itemView.findViewById(R.id.tvAnswer);
            markEditText = itemView.findViewById(R.id.edtStudentMark);
            btn_grade = itemView.findViewById(R.id.btn_grade);
        }
    }
}
