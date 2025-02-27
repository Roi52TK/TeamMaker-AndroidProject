package com.roi.teammeet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class UserEditAdapter extends RecyclerView.Adapter<UserEditAdapter.UserViewHolder> {

    private static final String TAG = "UserEditAdapter";
    private Context context;
    private List<User> userList;
    private DatabaseService databaseService;

    public UserEditAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setFilteredList(List<User> filteredList){
        this.userList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());
        holder.etUsername.setText(user.getUsername());
        holder.etBirthYear.setText(user.getBirthYear());
        holder.etGender.setText(user.getGender());
        holder.etPhone.setText(user.getPhone());
        holder.etEmail.setText(user.getEmail());
        holder.etPassword.setText(user.getPassword());

        databaseService = DatabaseService.getInstance();

        holder.btnUpdate.setOnClickListener(v -> {
            String userName = holder.etUsername.getText().toString();
            String birthYear = holder.etBirthYear.getText().toString();
            String gender = holder.etGender.getText().toString();
            String phone = holder.etPhone.getText().toString();
            String email = holder.etEmail.getText().toString();
            String password = holder.etPassword.getText().toString();

            User updatedUser = new User(user.getId(), userName, birthYear, gender, phone, email, password);

            databaseService.createNewUser(updatedUser, new DatabaseService.DatabaseCallback<Object>() {
                @Override
                public void onCompleted(Object object) {

                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });

        holder.btnDelete.setOnClickListener(v -> {
            databaseService.deleteUser(user.getId(), new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {

                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        EditText etUsername;
        EditText etBirthYear;
        EditText etGender;
        EditText etPhone;
        EditText etEmail;
        EditText etPassword;
        Button btnUpdate;
        Button btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername_userCard);
            etUsername = itemView.findViewById(R.id.etUsername_userCard);
            etBirthYear = itemView.findViewById(R.id.etBirthYear_userCard);
            etGender = itemView.findViewById(R.id.etGender_userCard);
            etPhone = itemView.findViewById(R.id.etPhone_userCard);
            etEmail = itemView.findViewById(R.id.etEmail_userCard);
            etPassword = itemView.findViewById(R.id.etPassword_userCard);
            btnUpdate = itemView.findViewById(R.id.btnUpdate_userCard);
            btnDelete = itemView.findViewById(R.id.btnDelete_userCard);
        }
    }
}

