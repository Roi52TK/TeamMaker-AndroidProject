package com.roi.teammeet.screens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.MyApplication;
import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.ActivityCollector;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.SharedPreferencesUtil;
import com.roi.teammeet.utils.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "RegisterActivity";

    private EditText etUsername, etBirthDate, etPhone, etEmail, etPassword;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;
    private String chosenDate;
    private String chosenGender;
    private Button btnRegister;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initViews();

        btnRegister.setOnClickListener(this);
    }

    private void initViews(){
        etUsername = findViewById(R.id.etUsername_register);
        etBirthDate = findViewById(R.id.etBirthDate_register);
        etBirthDate.setOnClickListener(this);

        rgGender = findViewById(R.id.rgGender_register);
        rgGender.setOnCheckedChangeListener(this);
        rbMale = findViewById(R.id.rbMaleGender_register);
        rbFemale = findViewById(R.id.rbFemaleGender_register);
        rbOther = findViewById(R.id.rbOtherGender_register);

        etPhone = findViewById(R.id.etPhone_register);
        etEmail = findViewById(R.id.etEmail_register);
        etPassword = findViewById(R.id.etPassword_register);
        btnRegister = findViewById(R.id.btnSignUp_register);

        rgGender.check(rbOther.getId());
    }

    private void createDateDialog(){
        String today = DateUtil.getToday();

        DatePickerDialog dialog = new DatePickerDialog(
                RegisterActivity.this,
                R.style.CustomDatePickerDialog, // Apply custom style here
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etBirthDate.setText(chosenDate);
                    }
                },
                DateUtil.getYear(today),
                DateUtil.getMonth(today),
                DateUtil.getDay(today)
        );

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == etBirthDate){
            createDateDialog();
        }
        else if (v == btnRegister) {
            Log.d(TAG, "onClick: Register button clicked");

            String username = etUsername.getText().toString();
            String phone = etPhone.getText().toString();
            String birthDate = etBirthDate.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            Log.d(TAG, "onClick: Username: " + username);
            Log.d(TAG, "onClick: Phone: " + phone);
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);

            // Validate input
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInput(username, phone, email, password)) {
                return;
            }

            Log.d(TAG, "onClick: Registering user...");

            // Register user
            registerUser(username,birthDate, phone, email, password);
        }
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

    private void registerUser(String username, String birthDate, String phone, String email, String password) {
        // Register user
        Log.d(TAG, "registerUser: Registering user...");

        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback() {

            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: User registered successfully");
                String uid = authenticationService.getCurrentUser().getUid();
                User user = new User(uid, username, birthDate, chosenGender, phone, email, password, false);
                databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Object>() {
                    @Override
                    public void onCompleted(Object object) {
                        Log.d(TAG, "onCompleted: User registered successfully");
                        SharedPreferencesUtil.saveUser(RegisterActivity.this, user);
                        // Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                        Log.d(TAG, "onCompleted: Redirecting to MainActivity");
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                        ActivityCollector.finishAll();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to register user", e);
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to register user", e);

            }
        });


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        if(id == rbMale.getId()){
            chosenGender = "זכר";
        }
        else if(id == rbFemale.getId()){
            chosenGender = "נקבה";
        }
        else if(id == rbOther.getId()){
            chosenGender = "אחר";
        }
    }
}