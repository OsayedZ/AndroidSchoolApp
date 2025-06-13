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
import com.example.androidschoolapp.activities.common.BaseActivity;
import com.example.androidschoolapp.activities.registrar.RegistrarClass;
import com.example.androidschoolapp.activities.registrar.RegistrarClassesActivity;
import com.example.androidschoolapp.activities.registrar.RegistrarManageClass;
import com.example.androidschoolapp.models.Class;
import com.google.gson.Gson;
import com.example.androidschoolapp.api.ApiClient;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassViewHolder> {
    private Context context;

    private List<Class> classes;
    private ApiClient apiClient;

    public ClassesAdapter(Context context, List<Class> classes) {
        this.context = context;
        this.classes = classes;
        this.apiClient = ApiClient.getInstance(context);
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class item = classes.get(position);

        // display class name
        holder.className.setText(item.getName());

        // Handle edit event
        holder.btn_edit.setOnClickListener(e -> {
            Intent intent = new Intent(context, RegistrarManageClass.class);
            Gson gson = new Gson();
            intent.putExtra("Edit", true);
            intent.putExtra("Class", gson.toJson(item));
            context.startActivity(intent);
        });

        // Handle delete event
        holder.btn_delete.setOnClickListener(e -> {
            apiClient.deleteClass(item.getId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        classes.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                    }
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        });

        holder.class_card.setOnClickListener(e -> {
            Intent intent = new Intent(context, RegistrarClass.class);
            Gson gson = new Gson();
            intent.putExtra("Class", gson.toJson(item));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView className;
        public Button btn_edit;
        public Button btn_delete;

        public CardView class_card;

        public ClassViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.txt_name);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            class_card = itemView.findViewById(R.id.class_card);
        }
    }
}
