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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject_t, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject currentSubject = Subjects.get(position);

        holder.SubjectNameTextView.setText("Subjec: " + currentSubject.getName());
        holder.SubjectIdTextView.setText("ID: " + String.valueOf(currentSubject.getId()));
        holder.SubjectDayTextView.setText("Day: " + convertNumberToDay(currentSubject.getDay()));
        holder.SubjectStartTextView.setText("Start: " + currentSubject.getStartTime());
        holder.SubjectEndTextView.setText("End: " + currentSubject.getEndTime());

    }

    private String convertNumberToDay(int number) {
        switch (number) {
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            default: return "Invalid day";
        }
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
