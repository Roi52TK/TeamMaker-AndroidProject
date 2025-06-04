package com.roi.teammeet.screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.utils.ActivityCollector;
import com.roi.teammeet.utils.SharedPreferencesUtil;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    private TextView tvWelcome;

    private Button btnSearchMatches;
    private Button btnMyMatches;
    private Button btnCreateMatch;
    private Button btnAdminUserEdit;
    private Button btnAdminMatchEdit;
    private Button btnSignOut;
    private Button btnProfile;
    private String welcomeMsg;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();

        if (!authenticationService.isUserSignedIn()) {
            Log.d(TAG, "User not signed in, redirecting to LandingActivity");
            Intent landingIntent = new Intent(MainActivity.this, LandingActivity.class);
            startActivity(landingIntent);
            finish();
        }

        initViews();
        setListeners();

        currentUser = SharedPreferencesUtil.getUser(this);
        if (currentUser != null) {
            welcomeMsg = "Welcome back " + currentUser.getUsername() + "!";
            tvWelcome.setText(welcomeMsg);

            if(!currentUser.isAdmin()){
                ((ViewGroup) btnAdminUserEdit.getParent()).removeView(btnAdminUserEdit);
                ((ViewGroup) btnAdminMatchEdit.getParent()).removeView(btnAdminMatchEdit);
            }
        }

        createNotificationChannel();

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome_main);
        btnSearchMatches = findViewById(R.id.btnSearchMatches_main);
        btnMyMatches = findViewById(R.id.btnMyMatches_main);
        btnCreateMatch = findViewById(R.id.btnCreateMatch_main);
        btnAdminUserEdit = findViewById(R.id.btnAdminUserEdit_main);
        btnAdminMatchEdit = findViewById(R.id.btnAdminMatchEdit_main);
        btnSignOut = findViewById(R.id.btnSignOut_main);
        btnProfile = findViewById(R.id.btnProfile_main);
    }

    private void setListeners(){
        btnSearchMatches.setOnClickListener(this);
        btnMyMatches.setOnClickListener(this);
        btnCreateMatch.setOnClickListener(this);
        btnAdminUserEdit.setOnClickListener(this);
        btnAdminMatchEdit.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSearchMatches){
            Intent availableMatchesIntent = new Intent(this, SearchMatchesActivity.class);
            startActivity(availableMatchesIntent);
        }
        else if(v == btnMyMatches){
            Intent myMatchesIntent = new Intent(this, MyMatchesActivity.class);
            startActivity(myMatchesIntent);
        }
        else if(v == btnCreateMatch){
            Intent createMatchIntent = new Intent(this, NewMatchActivity.class);
            startActivity(createMatchIntent);
        }
        else if(v == btnAdminUserEdit){
            Intent adminUserEditIntent = new Intent(this, AdminUserEditActivity.class);
            startActivity(adminUserEditIntent);
        }
        else if(v == btnAdminMatchEdit){
            Intent adminMatchEditIntent = new Intent(this, AdminMatchEditActivity.class);
            startActivity(adminMatchEditIntent);
        }
        else if(v == btnSignOut){
            SignOut();
            Intent landingIntent = new Intent(this, LandingActivity.class);
            startActivity(landingIntent);
            finish();
        }
        else if(v == btnProfile){
            Intent profileIntent = new Intent(this, MyProfileActivity.class);
            startActivity(profileIntent);
        }
    }

    // Create a notification channel for devices running API 26 (Oreo) and above
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Match Notifications";
            String description = "Notifications for upcoming matches";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("match_notifications_channel", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}