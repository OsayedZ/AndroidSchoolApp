package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.registrar.RegistrarAssignTeacher;
import com.example.androidschoolapp.activities.registrar.RegistrarManageClass;
import com.example.androidschoolapp.activities.registrar.RegistrarManageSubject;
import com.example.androidschoolapp.activities.registrar.RegistrarSubjectsActivity;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.Class;
import com.example.androidschoolapp.models.Subject;
import com.google.gson.Gson;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectViewHolder> {

    private Context context;

    private List<Subject> subjects;
    private ApiClient apiClient;

    public SubjectsAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
        this.apiClient = ApiClient.getInstance(context);
    }

    @NonNull
    @Override
    public SubjectsAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject item = subjects.get(position);

        // display class name

        holder.subjectName.setText(item.getName());
        holder.teacherName.setText(item.getTeacherName());
        holder.startTime.setText("Start: " + item.getStartTime());
        holder.endTime.setText("End: " + item.getEndTime());


        holder.btn_edit.setOnClickListener(e -> {
            Intent intent = new Intent(context, RegistrarManageSubject.class);
            Gson gson = new Gson();
            intent.putExtra("Edit", true);
            intent.putExtra("Subject", gson.toJson(item));
            context.startActivity(intent);
        });

        holder.btn_delete.setOnClickListener(e -> {
            apiClient.deleteSubject(item.getId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        subjects.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        });

        holder.subject_card.setOnClickListener(e -> {
            Intent intent = new Intent(context, RegistrarAssignTeacher.class);
            Gson gson = new Gson();
            intent.putExtra("Subject", gson.toJson(item));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectName;

        public TextView teacherName;
        public TextView startTime;
        public TextView endTime;

        public Button btn_edit;
        public Button btn_delete;

        public CardView subject_card;

        public SubjectViewHolder(View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.subject_name);
            teacherName = itemView.findViewById(R.id.teacher_name);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            subject_card = itemView.findViewById(R.id.subject_card);

        }
    }
}
