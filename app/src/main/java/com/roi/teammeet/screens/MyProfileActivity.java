package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.ActivityCollector;
import com.roi.teammeet.utils.SharedPreferencesUtil;
import com.roi.teammeet.utils.Validator;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "MyProfileActivity";
    TextView tvUsername, tvBirthDate, tvPhone, tvEmail;
    EditText etUsername, etPhone, etEmail;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale, rbOther;
    String chosenGender;
    Button btnConfirm;
    User user;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
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
        tvBirthDate = findViewById(R.id.tvBirthDate_myProfile);
        tvPhone = findViewById(R.id.tvPhone_myProfile);
        tvEmail = findViewById(R.id.tvEmail_myProfile);

        etUsername = findViewById(R.id.etUsername_myProfile);

        rgGender = findViewById(R.id.rgGender_myProfile);
        rbMale = findViewById(R.id.rbMaleGender_myProfile);
        rbFemale = findViewById(R.id.rbFemaleGender_myProfile);
        rbOther = findViewById(R.id.rbOtherGender_myProfile);

        etPhone = findViewById(R.id.etPhone_myProfile);
        etEmail = findViewById(R.id.etEmail_myProfile);

        etEmail.setEnabled(false);

        btnConfirm = findViewById(R.id.btnConfirm_myProfile);
        btnConfirm.setOnClickListener(this);
    }

    private void initData() {
        etUsername.setText(user.getUsername());
        tvBirthDate.setText("תאריך לידה: " + user.getBirthDate());
        setCheckedGender();
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());

        chosenGender = user.getGender();
        rgGender.setOnCheckedChangeListener(this);
    }

    private void setCheckedGender() {
        int id;

        if(user.getGender().equals("male")){
            id = rbMale.getId();
        }
        else if(user.getGender().equals("female")){
            id = rbFemale.getId();
        }
        else{
            id = rbOther.getId();
        }

        rgGender.check(id);
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
        user.setGender(chosenGender);

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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {

        if(id == rbMale.getId()){
            chosenGender = "male";
        }
        else if(id == rbFemale.getId()){
            chosenGender = "female";
        }
        else if(id == rbOther.getId()){
            chosenGender = "other";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}