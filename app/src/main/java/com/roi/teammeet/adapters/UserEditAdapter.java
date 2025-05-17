package com.roi.teammeet.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.MyApplication;
import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.screens.RegisterActivity;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.Validator;

import java.util.ArrayList;
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
        holder.etBirthDate.setText(user.getBirthDate());

        //TODO: Place it in another class as static / change to enum
        ArrayList<String> genderArrayList = new ArrayList<>();
        genderArrayList.add("male");
        genderArrayList.add("female");
        genderArrayList.add("other");

        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, genderArrayList);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerGender.setAdapter(genderSpinnerAdapter);

        holder.etPhone.setText(user.getPhone());
        holder.etEmail.setText(user.getEmail());
        holder.etPassword.setText(user.getPassword());
        holder.switchAdmin.setChecked(user.isAdmin());

        holder.etEmail.setEnabled(false);
        holder.etPassword.setEnabled(false);

        holder.etBirthDate.setOnClickListener(view -> {
            holder.createDateDialog(context);
        });

        databaseService = DatabaseService.getInstance();

        holder.btnUpdate.setOnClickListener(v -> {
            String username = holder.etUsername.getText().toString();
            String birthDate = holder.etBirthDate.getText().toString();
            String gender = holder.spinnerGender.getSelectedItem().toString();
            String phone = holder.etPhone.getText().toString();
            String email = holder.etEmail.getText().toString();
            String password = holder.etPassword.getText().toString();
            boolean isAdmin = holder.switchAdmin.isChecked();

            Log.d(TAG, "onClick: Validating input...");
            if (!holder.checkInput(username, phone, email, password)) {
                return;
            }

            User updatedUser = new User(user.getId(), username, birthDate, gender, phone, email, password, isAdmin);

            databaseService.createNewUser(updatedUser, new DatabaseService.DatabaseCallback<Object>() {
                @Override
                public void onCompleted(Object object) {
                    Toast.makeText(context, "Successfully updated user.", Toast.LENGTH_SHORT).show();
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
        EditText etBirthDate;
        String chosenDate;
        Spinner spinnerGender;
        EditText etPhone;
        EditText etEmail;
        EditText etPassword;
        SwitchCompat switchAdmin;
        Button btnUpdate;
        Button btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsernameTitle_userCard);
            etUsername = itemView.findViewById(R.id.etUsername_userCard);
            etBirthDate = itemView.findViewById(R.id.etBirthDate_userCard);
            spinnerGender = itemView.findViewById(R.id.spinnerGender_userCard);
            etPhone = itemView.findViewById(R.id.etPhone_userCard);
            etEmail = itemView.findViewById(R.id.etEmail_userCard);
            etPassword = itemView.findViewById(R.id.etPassword_userCard);
            switchAdmin = itemView.findViewById(R.id.switchAdmin_userCard);
            btnUpdate = itemView.findViewById(R.id.btnUpdate_userCard);
            btnDelete = itemView.findViewById(R.id.btnDelete_userCard);
        }

        private void createDateDialog(Context context){

            chosenDate = etBirthDate.getText().toString();

            DatePickerDialog dialog = new DatePickerDialog(
                    context,
                    R.style.CustomDatePickerDialog, // Apply custom style here
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                            etBirthDate.setText(chosenDate);
                        }
                    },
                    DateUtil.getYear(chosenDate),
                    DateUtil.getMonth(chosenDate),
                    DateUtil.getDay(chosenDate)
            );

            dialog.show();
        }

        private boolean checkInput(String username, String phone, String email, String password) {
            if (!Validator.isUsernameValid(username)) {
                Log.e(TAG, "checkInput: Username must be at least 3 characters long");
                etUsername.setError("Username must be at least 3 characters long");
                etUsername.requestFocus();
                return false;
            }

            if(!Validator.isBirthDateValid(chosenDate)){
                Log.e(TAG, "checkInput: Age must be at least " + MyApplication.AGE_LIMIT);
                etBirthDate.setError("Age must be at least " + MyApplication.AGE_LIMIT);
                etBirthDate.requestFocus();
                return false;
            }

            if(!Validator.isPhoneValid(phone)){
                Log.e(TAG, "checkInput: Phone number must be 10 digits long");
                etPhone.setError("Phone number must be 10 digits long");
                etPhone.requestFocus();
                return false;
            }

            if (!Validator.isEmailValid(email)) {
                Log.e(TAG, "checkInput: Invalid email address");
                etEmail.setError("Invalid email address");
                etEmail.requestFocus();
                return false;
            }

            if (!Validator.isPasswordValid(password)) {
                Log.e(TAG, "checkInput: Password must be at least 8 characters long");
                etPassword.setError("Password must be at least 8 characters long");
                etPassword.requestFocus();
                return false;
            }

            return true;
        }
    }
}

