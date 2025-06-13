package com.example.androidschoolapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.teacher.StudentsInClassActivity;
import com.example.androidschoolapp.activities.teacher.SubjectsOfClassActivity;
import com.example.androidschoolapp.models.Class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<Class> classes;


    public ClassAdapter(List<Class> classes) {
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_teacher, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class currentClass = classes.get(position);
        holder.classNameTextView.setText("Name: " + currentClass.getName());
        holder.classIdTextView.setText("ID: " + currentClass.getId());
//        holder.studentsTextView.setText("Number Of Students: " + currentClass.getNumberOfStudent());
//        holder.subjectsTextView.setText("Number Of Subjects" + currentClass.getNumberOfSubjects());

        holder.studentsBtn.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), StudentsInClassActivity.class);

            intent.putExtra("CLASS_NAME", currentClass.getName());
            intent.putExtra("CLASS_ID", String.valueOf(currentClass.getId()));

            v.getContext().startActivity(intent);
        });

        holder.subjectsBtn.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), SubjectsOfClassActivity.class);

            intent.putExtra("CLASS_NAME", currentClass.getName());
            intent.putExtra("CLASS_ID", String.valueOf(currentClass.getId()));

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;
        TextView classIdTextView;
        TextView studentsTextView;
        TextView subjectsTextView;
        Button studentsBtn;
        Button subjectsBtn;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.class_name);
            classIdTextView = itemView.findViewById(R.id.class_id);
            studentsTextView = itemView.findViewById(R.id.num_of_students);
            subjectsTextView = itemView.findViewById(R.id.num_of_subjects);
            studentsBtn = itemView.findViewById(R.id.btnStudents);
            subjectsBtn = itemView.findViewById(R.id.btnSubjects);
        }
    }
}