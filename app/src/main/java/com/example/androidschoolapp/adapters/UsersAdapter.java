package com.example.androidschoolapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidschoolapp.R;
import com.example.androidschoolapp.activities.registrar.RegistrarAssignClass;
import com.example.androidschoolapp.activities.registrar.RegistrarManageST;
import com.example.androidschoolapp.api.ApiClient;
import com.example.androidschoolapp.models.User;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private Context context;
    private List<User> users;
    private ApiClient apiClient;


    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.apiClient = ApiClient.getInstance(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User item = users.get(position);
        holder.user_name.setText("Name: " + item.getName());
        holder.user_email.setText("Email: " + item.getEmail());
        holder.user_age.setText("Age: " + item.getAge());
        holder.user_gender.setText("Gender: " + item.getGenderString());

        if(item.getRole().equals("Student")) {
            holder.card_student.setOnClickListener(e -> {
                Intent intent = new Intent(context, RegistrarAssignClass.class);
                intent.putExtra("StudentID", String.valueOf(item.getId()));
                context.startActivity(intent);
            });
        }

        holder.btn_edit.setOnClickListener(e -> {
            Intent intent = new Intent(context, RegistrarManageST.class);
            Gson gson = new Gson();
            intent.putExtra("Edit", true);
            intent.putExtra("User", gson.toJson(item));
            context.startActivity(intent);
        });

        holder.btn_delete.setOnClickListener(e -> {

            apiClient.deleteStudentOrTeacher(item.getId(), new ApiClient.DataCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        users.remove(currentPosition);
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
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView user_name;
        private TextView user_email;
        private TextView user_age;
        private TextView user_gender;

        private Button btn_edit;
        private Button btn_delete;

        private CardView card_student;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_email = itemView.findViewById(R.id.user_email);
            user_age = itemView.findViewById(R.id.user_age);
            user_gender = itemView.findViewById(R.id.user_gender);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            card_student = itemView.findViewById(R.id.card_student);
        }
    }
}
