package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.ClassSubject;
import com.example.androidschoolapp.models.Subject;

import java.util.List;

public class SubjectsClassAdapter extends RecyclerView.Adapter<SubjectsClassAdapter.SubjectClassViewHolder> {

    private Context context;
    private List<Subject> subjects;
    private ApiClient apiClient;
    private int classID;
    public SubjectsClassAdapter(Context context, ClassSubject subject) {
        this.context = context;
        this.subjects = subject.getSubjects();
        this.apiClient = ApiClient.getInstance(context);
        this.classID = subject.getClassId();
    }

    @NonNull
    @Override
    public SubjectClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_class, parent, false);
        return new SubjectsClassAdapter.SubjectClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectClassViewHolder holder, int position) {
        Subject item = subjects.get(position);
        holder.subject_name.setText(item.getName());

        holder.btn_unassign.setOnClickListener(e -> {

            apiClient.unassignSubjectFromClass(item.getId(), classID, new ApiClient.DataCallback<String>() {
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
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectClassViewHolder extends RecyclerView.ViewHolder {
        public TextView subject_name;

        public Button btn_unassign;

        public SubjectClassViewHolder(View itemView) {
            super(itemView);
            subject_name = itemView.findViewById(R.id.subject_name);
            btn_unassign = itemView.findViewById(R.id.btn_unassign);
        }
    }
}
