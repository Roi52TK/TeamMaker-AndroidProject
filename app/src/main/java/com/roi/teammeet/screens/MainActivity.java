package com.roi.teammeet.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;

    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
        btnSignOut.setOnClickListener(this);
    }

    private void initViews() {
        btnSignOut = findViewById(R.id.btnSignOut);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSignOut){
            authenticationService.signOut();
            SharedPreferencesUtil.signOutUser(this);
            Intent landingIntent = new Intent(this, LandingActivity.class);
            startActivity(landingIntent);
            finish();
        }
    }
}