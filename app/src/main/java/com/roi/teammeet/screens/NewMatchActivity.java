package com.roi.teammeet.screens;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.roi.teammeet.R;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.MatchValidator;
import com.roi.teammeet.utils.Validator;

public class NewMatchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewMatchActivity";

    EditText etTitle;
    EditText etDetails;
    Button btnDate;
    EditText etTime;
    EditText etCity;
    EditText etMinAge;
    EditText etMaxAge;
    EditText etSize;
    String chosen_date;
    TextView tvDate;
    Button btnCreate;
    boolean is_date_picked;


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
        btnDate.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle_newMatch);
        etDetails = findViewById(R.id.etDetails_newMatch);
        btnDate = findViewById(R.id.btnDate_newMatch);
        etTime = findViewById(R.id.etTime_newMatch);
        etCity = findViewById(R.id.etCity_newMatch);
        etMinAge = findViewById(R.id.etMinAge_newMatch);
        etMaxAge = findViewById(R.id.etMaxAge_newMatch);
        etSize = findViewById(R.id.etSize_newMatch);
        tvDate = findViewById(R.id.tvDate_newMatch);
        btnCreate = findViewById(R.id.btnCreate_newMatch);
    }

    private void createDialog(){
        String today = DateUtil.getToday();
        DatePickerDialog dialog = new DatePickerDialog(NewMatchActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                chosen_date = dayOfMonth + "/" + (month+1) + "/" + year;
                is_date_picked = true;
                tvDate.setText(chosen_date);
            }
        }, DateUtil.getYear(today), DateUtil.getMonth(today), DateUtil.getDay(today));

        dialog.show();
    }

    private boolean checkInput(String title, String details, String time, String city, int min, int max, int size){
        if (!MatchValidator.isTitleValid(title)) {
            Log.e(TAG, "checkInput: Title must be between 4-12 characters long");
            etTitle.setError("Title must be between 4-12 characters long");
            etTitle.requestFocus();
            return false;
        }

        //TODO more MatchValidator input checks

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == btnDate){
            createDialog();
        }
        if(v == btnCreate){
            String title = etTitle.getText().toString();
            String details = etDetails.getText().toString();
            String time = etTime.getText().toString();
            String city = etCity.getText().toString();
            int min = Integer.parseInt(etMinAge.getText().toString());
            int max = Integer.parseInt(etMaxAge.getText().toString());
            int size = Integer.parseInt(etSize.getText().toString());
            //TODO add address by the map

            if(!checkInput(title, details, time, city, min, max, size)){
                return;
            }
            
            createMatch(title, details, time, city, min, max, size);
        }
    }

    private void createMatch(String title, String details, String time, String city, int min, int max, int size) {
        //TODO createNewMatch through DatabaseService
    }
}