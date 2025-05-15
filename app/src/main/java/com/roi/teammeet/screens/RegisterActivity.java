package com.roi.teammeet.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.SharedPreferencesUtil;
import com.roi.teammeet.utils.Validator;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText etUsername, etPhone, etEmail, etPassword;
    private Spinner spinnerBirthYear, spinnerGender;
    private Button btnRegister;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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

        initBirthYearsSpinner();
        initGenderSpinner();
    }

    private void initGenderSpinner() {
        ArrayList<String> genderArrayList = new ArrayList<>();
        genderArrayList.add("male");
        genderArrayList.add("female");
        genderArrayList.add("other");

        ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderArrayList);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderSpinnerAdapter);
    }

    private void initBirthYearsSpinner() {
        ArrayAdapter<String> birthYearsSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DateUtil.birthYearsArray());
        birthYearsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBirthYear.setAdapter(birthYearsSpinnerAdapter);
    }

    private void initViews(){
        etUsername = findViewById(R.id.etUsername_register);
        etPhone = findViewById(R.id.etPhone_register);
        etEmail = findViewById(R.id.etEmail_register);
        etPassword = findViewById(R.id.etPassword_register);
        btnRegister = findViewById(R.id.btnSignUp_register);

        spinnerBirthYear = findViewById(R.id.spinnerBirthYear_register);
        spinnerGender = findViewById(R.id.spinnerGender_register);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            Log.d(TAG, "onClick: Register button clicked");

            String username = etUsername.getText().toString();
            String phone = etPhone.getText().toString();
            String birthYear = spinnerBirthYear.getSelectedItem().toString();
            String gender = spinnerGender.getSelectedItem().toString();
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
            registerUser(username,birthYear, gender, phone, email, password);
            return;
        }
    }

    private boolean checkInput(String username, String phone, String email, String password) {
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

    private void registerUser(String username, String birthYear, String gender, String phone, String email, String password) {
        // Register user
        Log.d(TAG, "registerUser: Registering user...");

        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback() {

            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: User registered successfully");
                String uid = authenticationService.getCurrentUser().getUid();
                User user = new User(uid, username, birthYear, gender, phone, email, password, false);
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
}