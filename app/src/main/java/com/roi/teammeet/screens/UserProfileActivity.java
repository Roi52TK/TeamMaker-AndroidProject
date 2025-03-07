package com.roi.teammeet.screens;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvBirthYear;
    TextView tvGender;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
    }

    private void initData(){
        tvUsername.setText("Username: " + user.getUsername().toString());
        tvBirthYear.setText("Birth Year: " + user.getBirthYear().toString());
        tvGender.setText("Gender: " + user.getGender().toString());
    }
}