package com.roi.teammeet.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.SharedPreferencesUtil;
import com.roi.teammeet.utils.Validator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail, etPassword;
    Button btnLogin;

    private static final String TAG = "LoginActivity";

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initViews();

        btnLogin.setOnClickListener(this);
    }

    private void initViews(){
        etEmail = findViewById(R.id.etEmail_login);
        etPassword = findViewById(R.id.etPassword_login);
        btnLogin = findViewById(R.id.btnLogin_login);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogin.getId()) {
            Log.d(TAG, "onClick: Login button clicked");

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);

            Log.d(TAG, "onClick: Validating input...");
            // Validate input
            if (!checkInput(email, password)) {
                return;
            }

            Log.d(TAG, "onClick: Logging in user...");

            // Login user
            loginUser(email, password);
        }
    }

    private boolean checkInput(String email, String password) {
        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            etEmail.setError("Invalid email address");
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Log.e(TAG, "checkInput: Password must be at least 6 characters long");
            etPassword.setError("Password must be at least 6 characters long");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser(String email, String password) {
        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback() {

            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: User logged in successfully");
                String uid = authenticationService.getCurrentUser().getUid();
                String phone = authenticationService.getCurrentUser().getPhoneNumber();
                databaseService.getUser(uid, new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {
                        SharedPreferencesUtil.saveUser(LoginActivity.this, user);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to login user", e);
                    }
                });
                // Redirect to main activity and clear back stack to prevent user from going back to login screen
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to log in user", e);
                // Show error message to user
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
            }
        });
    }
}