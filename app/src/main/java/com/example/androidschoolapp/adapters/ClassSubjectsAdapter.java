package com.example.androidschoolapp.adapters;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.models.Subject;

import java.util.List;

public class ClassSubjectsAdapter extends RecyclerView.Adapter<ClassSubjectsAdapter.SubjectViewHolder>{


    private List<Subject> Subjects;


    public ClassSubjectsAdapter(List<Subject> Subjects) {
        this.Subjects = Subjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject currentSubject = Subjects.get(position);
        Log.d(TAG, "Subject Data: " + currentSubject.getName() + ", " + currentSubject.getId());


        holder.SubjectNameTextView.append(currentSubject.getName());
        holder.SubjectIdTextView.append(String.valueOf(currentSubject.getId()));
        holder.SubjectDayTextView.append(String.valueOf(currentSubject.getDay()));
        holder.SubjectStartTextView.append(currentSubject.getStartTime());
        holder.SubjectEndTextView.append(currentSubject.getEndTime());

    }




    @Override
    public int getItemCount() {
        return Subjects.size(); // Fixed: Return the actual size of the list
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView SubjectNameTextView;
        TextView SubjectIdTextView;
        TextView SubjectDayTextView;
        TextView SubjectStartTextView;
        TextView SubjectEndTextView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            SubjectNameTextView = itemView.findViewById(R.id.subject_name);
            SubjectIdTextView = itemView.findViewById(R.id.subject_id);
            SubjectDayTextView = itemView.findViewById(R.id.subject_day);
            SubjectStartTextView = itemView.findViewById(R.id.subject_start);
            SubjectEndTextView = itemView.findViewById(R.id.subject_end);


        }

    }
}
