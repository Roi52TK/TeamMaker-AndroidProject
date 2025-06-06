package com.roi.teammeet.screens;

import android.os.Bundle;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.ActivityCollector;

public class UserProfileActivity extends BaseActivity {

    TextView tvUsername;
    TextView tvBirthYear;
    TextView tvGender;
    TextView tvPhone;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        String userId = getIntent().getStringExtra("userId");
        DatabaseService.getInstance().getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                user = object;
                initData();
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvUsername_userProfile);
        tvBirthYear = findViewById(R.id.tvBirthYear_userProfile);
        tvGender = findViewById(R.id.tvGender_userProfile);
        tvPhone = findViewById(R.id.tvPhone_userProfile);
    }

    private void initData(){
        tvUsername.setText("שם משתמש: " + user.getUsername().toString());
        tvBirthYear.setText("גיל: " + user.getAge());
        tvGender.setText("מין: " + user.getGender().toString());
        tvPhone.setText("מספר טלפון: " + user.getPhone().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}