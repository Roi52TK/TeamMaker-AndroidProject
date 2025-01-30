package com.roi.teammeet.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.MatchValidator;
import com.roi.teammeet.utils.SharedPreferencesUtil;

public class NewMatchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewMatchActivity";

    EditText etTitle;
    EditText etDetails;
    Button btnDate;
    TextView tvDate;
    Button btnTime;
    TextView tvTime;
    double lang, lat;
    String address;
    TextView tvAddress;
    EditText etMinAge;
    EditText etMaxAge;
    EditText etSize;
    String chosenDate;
    String chosenTime;
    Button btnCreate;
    Button btnMap;
    boolean isDatePicked;
    private DatabaseService databaseService;
    private User currentUser;


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

        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        initViews();
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        chosenDate = DateUtil.getToday();
        tvDate.setText(chosenDate);
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle_newMatch);
        etDetails = findViewById(R.id.etDetails_newMatch);
        btnDate = findViewById(R.id.btnDate_newMatch);
        tvDate = findViewById(R.id.tvDate_newMatch);
        btnTime = findViewById(R.id.btnTime_newMatch);
        tvTime = findViewById(R.id.tvTime_newMatch);
        etMinAge = findViewById(R.id.etMinAge_newMatch);
        etMaxAge = findViewById(R.id.etMaxAge_newMatch);
        etSize = findViewById(R.id.etSize_newMatch);
        btnMap = findViewById(R.id.btnMap_newMatch);
        tvAddress = findViewById(R.id.tvAddress_newMatch);
        btnCreate = findViewById(R.id.btnCreate_newMatch);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            // Retrieve the location data passed from MapsActivity
            String langSt = data.getStringExtra("lang");
            String latSt = data.getStringExtra("lat");
            address = data.getStringExtra("address");

            if(langSt != null && latSt != null && address != null){
                lang = Double.parseDouble(langSt);
                lat = Double.parseDouble(latSt);

                tvAddress.setText(address);
            }
        }
    }

    private void createDateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(NewMatchActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                chosenDate = dayOfMonth + "/" + (month+1) + "/" + year;
                isDatePicked = true;
                tvDate.setText(chosenDate);
            }
        }, DateUtil.getYear(chosenDate), DateUtil.getMonth(chosenDate), DateUtil.getDay(chosenDate));

        dialog.show();
    }

    private void createTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(NewMatchActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                chosenTime = String.format("%d:%02d", i, i1);
                tvTime.setText(chosenTime);
            }
        }, 12, 0, true);

        dialog.show();
    }

    private boolean checkInput(String title, String details, String date, String time, String min, String max, String size){
        if (!MatchValidator.isTitleValid(title)) {
            Log.e(TAG, "checkInput: Title must be between 4-12 characters long");
            etTitle.setError("Title must be between 4-12 characters long");
            etTitle.requestFocus();
            return false;
        }

        if(!MatchValidator.isDetailsValid(details)){
            Log.e(TAG, "checkInput: Details must be 20 characters long at most");
            etDetails.setError("Details must be 20 characters long at most");
            etDetails.requestFocus();
            return false;
        }

        /*if(!MatchValidator.isDateValid(date)){

        }

        if(!MatchValidator.isTimeValid(time)){

        }*/

        if(!MatchValidator.isAddressValid(address)){
            Log.e(TAG, "checkInput: Address cannot be empty");
            tvAddress.setError("Address cannot be empty");
            tvAddress.requestFocus();
            return false;
        }

        if(!MatchValidator.isMinAgeValid(min)){
            int minAge = MatchValidator.MIN_AGE;
            Log.e(TAG, "checkInput: Minimum age must be at least " + minAge);
            etMinAge.setError("Minimum age must be at least " + minAge);
            etMinAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isAgeRangeValid(min, max)){
            Log.e(TAG, "checkInput: Maximum age must be bigger than or equal to minimum age");
            etMaxAge.setError("Maximum age must be bigger than or equal to minimum age");
            etMaxAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isUserAgeValid(min, max, currentUser.getBirthYear())){
            Log.e(TAG, "checkInput: Your age is not in range");
            etMaxAge.setError("Your age is not in range");
            etMaxAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isSizeValid(size)){
            Log.e(TAG, "checkInput: Size must be between 2-99");
            etSize.setError("Size must be between 2-99");
            etSize.requestFocus();
            return false;
        }

        //TODO more MatchValidator input checks

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == btnDate){
            createDateDialog();
        }
        if(v == btnTime){
            createTimeDialog();
        }
        if(v == btnMap){
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            startActivityForResult(mapsIntent, 200);
        }
        if(v == btnCreate){
            onClickCreate();
        }
    }

    private void onClickCreate() {
        String title = etTitle.getText().toString();
        String details = etDetails.getText().toString();
        String min = etMinAge.getText().toString();
        String max = etMaxAge.getText().toString();
        String size = etSize.getText().toString();


        if(!checkInput(title, details, chosenDate, chosenTime, min, max, size)){
            return;
        }

        int minNum = Integer.parseInt(min);
        int maxNum = Integer.parseInt(max);
        int sizeNum = Integer.parseInt(size);

        createMatch(title, details, minNum, maxNum, sizeNum);
    }

    private void createMatch(String title, String details, int min, int max, int size) {
        String matchId = databaseService.generateMatchId();
        Match newMatch = new Match(matchId, title, details, currentUser.getId(), chosenDate, chosenTime, lang, lat, address, min, max, size);
        databaseService.createNewMatch(newMatch, new DatabaseService.DatabaseCallback<Object>() {
            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: Created a match");
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to create match", e);
            }
        });
    }
}