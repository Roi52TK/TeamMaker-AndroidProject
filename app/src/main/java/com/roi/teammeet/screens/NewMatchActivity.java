package com.roi.teammeet.screens;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.roi.teammeet.R;

public class NewMatchActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDetails;
    EditText etDate;
    EditText etTime;
    EditText etCity;
    EditText etMinAge;
    EditText etMaxAge;
    EditText etSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle_newMatch);
        etDetails = findViewById(R.id.etDetails_newMatch);
        etDate = findViewById(R.id.etDate_newMatch);
        etTime = findViewById(R.id.etTime_newMatch);
        etCity = findViewById(R.id.etCity_newMatch);
        etMinAge = findViewById(R.id.etMinAge_newMatch);
        etMaxAge = findViewById(R.id.etMaxAge_newMatch);
        etSize = findViewById(R.id.etSize_newMatch);
    }
}