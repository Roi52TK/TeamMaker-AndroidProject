package com.roi.teammeet.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.R;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnReg, btnLogin, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        btnReg = findViewById(R.id.btnSignUp_landing);
        btnLogin = findViewById(R.id.btnLogin_landing);
        btnAbout = findViewById(R.id.btnAbout_landing);

        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnReg)
        {
            goToRegister();
        }
        else if(v == btnLogin)
        {
            goToLogin();
        }
        else if(v == btnAbout){
            goToAbout();
        }
    }

    private void goToRegister() {
        Intent go = new Intent(this, RegisterActivity.class);
        startActivity(go);
        finish();
    }

    private void goToLogin() {
        Intent go = new Intent(this, LoginActivity.class);
        startActivity(go);
        finish();
    }

    private void goToAbout() {
        Intent go = new Intent(this, AboutActivity.class);
        startActivity(go);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}