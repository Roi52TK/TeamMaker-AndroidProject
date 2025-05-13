package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.SharedPreferencesUtil;
import com.roi.teammeet.utils.Validator;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MyProfileActivity";
    TextView tvUsername, tvBirthYear, tvGender, tvPhone, tvEmail;
    EditText etUsername, etBirthYear, etGender, etPhone, etEmail;
    Button btnConfirm;
    User user;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        initViews();

        String userId = SharedPreferencesUtil.getUser(this).getId();

        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                Log.d(TAG, "onCompleted: User received successfully");
                user = object;
                initData();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to get user", e);
            }
        });
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername_myProfile);
        tvBirthYear = findViewById(R.id.tvBirthYear_myProfile);
        tvGender = findViewById(R.id.tvGender_myProfile);
        tvPhone = findViewById(R.id.tvPhone_myProfile);
        tvEmail = findViewById(R.id.tvEmail_myProfile);

        etUsername = findViewById(R.id.etUsername_myProfile);
        etBirthYear = findViewById(R.id.etBirthYear_myProfile);
        etGender = findViewById(R.id.etGender_myProfile);
        etPhone = findViewById(R.id.etPhone_myProfile);
        etEmail = findViewById(R.id.etEmail_myProfile);

        etBirthYear.setEnabled(false);
        etEmail.setEnabled(false);

        btnConfirm = findViewById(R.id.btnConfirm_myProfile);
        btnConfirm.setOnClickListener(this);
    }

    private void initData() {
        etUsername.setText(user.getUsername());
        etBirthYear.setText(user.getBirthYear());
        etGender.setText(user.getGender());
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
    }

    private boolean checkInput(String username, String phone) {
        if (!Validator.isUsernameValid(username)) {
            Log.e(TAG, "checkInput: Username must be at least 3 characters long");
            etUsername.setError("Username must be at least 3 characters long");
            etUsername.requestFocus();
            return false;
        }

        if(!Validator.isPhoneValid(phone)){
            Log.e(TAG, "checkInput: Phone number must be 10 digits long");
            etPhone.setError("Phone number must be 10 digits long");
            etPhone.requestFocus();
            return false;
        }

        //TODO: add spinner for Gender instead of edit text

        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnConfirm){
            OnConfirm();
        }
    }

    private void OnConfirm() {

        String username = etUsername.getText().toString();
        String phone = etPhone.getText().toString();

        if (!checkInput(username, phone)) {
            return;
        }

        user.setUsername(username);
        user.setPhone(phone);

        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Object>() {
            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: User updated successfully");
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to update user", e);
            }
        });

    }
}