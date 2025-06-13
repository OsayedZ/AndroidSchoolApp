package com.example.androidschoolapp.adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.User;

import java.util.List;

public class ClassStudentsAdapter extends RecyclerView.Adapter<ClassStudentsAdapter.StudentViewHolder> {

    private List<User> students;


    public ClassStudentsAdapter(List<User> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        User currentStudent = students.get(position);
        Log.d(TAG, "Student Data: " + currentStudent.getName() + ", " + currentStudent.getId());


        holder.studentNameTextView.setText(currentStudent.getName());
        holder.studentIdTextView.setText(String.valueOf(currentStudent.getId()));
        holder.studentEmailTextView.setText(currentStudent.getEmail());

    }




    @Override
    public int getItemCount() {
        return students.size(); // Fixed: Return the actual size of the list
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView studentIdTextView;
        TextView studentEmailTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.student_name);
            studentIdTextView = itemView.findViewById(R.id.student_id);
            studentEmailTextView = itemView.findViewById(R.id.student_email);

        }

    }
}