package com.roi.teammeet.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.SharedPreferencesUtil;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    private TextView tvWelcome;

    private Button btnAvailableMatches;
    private Button btnMyMatches;
    private Button btnCreateMatch;
    private Button btnAdminPage;
    private Button btnSignOut;
    private String welcomeMsg;
    private User currentUser;

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
        btnAvailableMatches.setOnClickListener(this);
        btnMyMatches.setOnClickListener(this);
        btnCreateMatch.setOnClickListener(this);
        btnAdminPage.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        currentUser = SharedPreferencesUtil.getUser(this);
        if (currentUser != null) {
            welcomeMsg = "Welcome back " + currentUser.getUsername() + "!";
            tvWelcome.setText(welcomeMsg);
        }
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome_main);
        btnAvailableMatches = findViewById(R.id.btnAvailableMatches_main);
        btnMyMatches = findViewById(R.id.btnMyMatches_main);
        btnCreateMatch = findViewById(R.id.btnCreateMatch_main);
        btnAdminPage = findViewById(R.id.btnAdminPage_main);
        btnSignOut = findViewById(R.id.btnSignOut_main);
    }

    @Override
    public void onClick(View v) {
        if(v == btnAvailableMatches){
            Intent availableMatchesIntent = new Intent(this, AvailableMatchesActivity.class);
            startActivity(availableMatchesIntent);
        }
        else if(v == btnMyMatches){
            Intent myMatchesIntent = new Intent(this, MyMatchesActivity.class);
            startActivity(myMatchesIntent);
        }
        else if(v == btnCreateMatch){
            Intent createMatchIntent = new Intent(this, NewMatchActivity.class);
            startActivity(createMatchIntent);
            finish();
        }
        else if(v == btnAdminPage){
            Intent adminPageIntent = new Intent(this, AdminPageActivity.class);
            startActivity(adminPageIntent);
            finish();
        }
        else if(v == btnSignOut){
            SignOut();
            Intent landingIntent = new Intent(this, LandingActivity.class);
            startActivity(landingIntent);
            finish();
        }
    }

    private void SignOut(){
        authenticationService.signOut();
        SharedPreferencesUtil.signOutUser(this);
    }
}