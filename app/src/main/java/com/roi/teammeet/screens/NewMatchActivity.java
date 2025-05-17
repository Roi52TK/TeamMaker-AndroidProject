package com.roi.teammeet.screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.roi.teammeet.MyApplication;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.MatchValidator;
import com.roi.teammeet.utils.ReminderUtils;
import com.roi.teammeet.utils.SharedPreferencesUtil;

public class NewMatchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NewMatchActivity";

    EditText etTitle;
    EditText etDescription;
    EditText etDate;
    EditText etTime;
    EditText etLocation;
    double lang, lat;
    String address;
    EditText etMinAge;
    EditText etMaxAge;
    EditText etSize;
    String chosenDate;
    String chosenTime;
    Button btnCreate;
    boolean isDatePicked;
    private DatabaseService databaseService;
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestLocationPermission();

        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        initViews();
        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLocation.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        chosenDate = DateUtil.getToday();
        etDate.setText(chosenDate);
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle_newMatch);
        etDescription = findViewById(R.id.etDescription_newMatch);
        etDate = findViewById(R.id.etDate_newMatch);
        etTime = findViewById(R.id.etTime_newMatch);
        etLocation = findViewById(R.id.etLocation_newMatch);
        etMinAge = findViewById(R.id.etMinAge_newMatch);
        etMaxAge = findViewById(R.id.etMaxAge_newMatch);
        etSize = findViewById(R.id.etSize_newMatch);
        btnCreate = findViewById(R.id.btnCreate_newMatch);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {
            // Retrieve the location data passed from MapsActivity
            String latSt = data.getStringExtra("lat");
            String langSt = data.getStringExtra("lang");
            address = data.getStringExtra("address");

            if(latSt != null && langSt != null && address != null){
                lat = Double.parseDouble(latSt);
                lang = Double.parseDouble(langSt);

                etLocation.setText(address);
            }
        }
    }

    private void createDateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(
                NewMatchActivity.this,
                R.style.CustomDatePickerDialog, // Apply custom style here
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chosenDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        isDatePicked = true;
                        etDate.setText(chosenDate);
                    }
                },
                DateUtil.getYear(chosenDate),
                DateUtil.getMonth(chosenDate),
                DateUtil.getDay(chosenDate)
        );

        dialog.show();
    }

    private void createTimeDialog(){
        TimePickerDialog dialog = new TimePickerDialog(
                NewMatchActivity.this,
                R.style.CustomTimePickerDialog, // Apply custom style here
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        chosenTime = String.format("%d:%02d", hourOfDay, minute);
                        etTime.setText(chosenTime);
                    }
                },
                12, 0, true
        );

        dialog.show();
    }

    private boolean checkInput(String title, String description, String min, String max, String size){
        if (!MatchValidator.isTitleValid(title)) {
            String range = MatchValidator.TITLE_MIN_LENGTH + "-" + MatchValidator.TITLE_MAX_LENGTH;
            Log.e(TAG, "checkInput: Title must be between " + range + " characters long");
            etTitle.setError("Title must be between " + range + " characters long");
            etTitle.requestFocus();
            return false;
        }

        if(!MatchValidator.isDescriptionValid(description)){
            Log.e(TAG, "checkInput: Description must be " + MatchValidator.DESCRIPTION_MAX_LENGTH + " characters long at most");
            etDescription.setError("Description must be " + MatchValidator.DESCRIPTION_MAX_LENGTH + " characters long at most");
            etDescription.requestFocus();
            return false;
        }

        if(!MatchValidator.isDateTimeValid(chosenDate, chosenTime)){
            Log.e(TAG, "checkInput: Date or time have already passed");
            etDate.setError("Date or time have already passed");
            etDate.requestFocus();
            return false;
        }

        if(!MatchValidator.isAddressValid(address)){
            Log.e(TAG, "checkInput: Address cannot be empty");
            etLocation.setError("Address cannot be empty");
            etLocation.requestFocus();
            return false;
        }

        if(!MatchValidator.isMinAgeValid(min)){
            Log.e(TAG, "checkInput: Minimum age must be at least " + MyApplication.AGE_LIMIT);
            etMinAge.setError("Minimum age must be at least " + MyApplication.AGE_LIMIT);
            etMinAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isAgeRangeValid(min, max)){
            Log.e(TAG, "checkInput: Maximum age must be bigger than or equal to minimum age");
            etMaxAge.setError("Maximum age must be bigger than or equal to minimum age");
            etMaxAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isUserAgeValid(min, max, currentUser.getBirthDate())){
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

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == etDate){
            createDateDialog();
        }

        else if(v == etTime){
            createTimeDialog();
        }
        else if(v == etLocation){
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            mapsIntent.putExtra("lat", String.valueOf(lat));
            mapsIntent.putExtra("lang", String.valueOf(lang));
            startActivityForResult(mapsIntent, 200);
        }
        else if(v == btnCreate){
            onClickCreate();
        }
    }

    private void onClickCreate() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String min = etMinAge.getText().toString();
        String max = etMaxAge.getText().toString();
        String size = etSize.getText().toString();


        if(!checkInput(title, description, min, max, size)){
            return;
        }

        int minNum = Integer.parseInt(min);
        int maxNum = Integer.parseInt(max);
        int sizeNum = Integer.parseInt(size);

        createMatch(title, description, minNum, maxNum, sizeNum);

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void createMatch(String title, String description, int min, int max, int size) {
        String matchId = databaseService.generateMatchId();
        Match newMatch = new Match(matchId, title, description, currentUser.getId(), chosenDate, chosenTime, lat, lang, address, min, max, size);
        databaseService.createNewMatch(newMatch, new DatabaseService.DatabaseCallback<Object>() {
            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: Created a match");

                //long meetingTimeMillis = ReminderUtils.dateTimeToMilliseconds(match.getDate(), match.getTime());
                //ReminderUtils.scheduleMeetingReminder(context, meetingTimeMillis, match.getTitle(), match.getTime(), currentUser.getUsername(), match.getId());

                //TODO: change the meeting time
                ReminderUtils.scheduleMeetingReminder(NewMatchActivity.this, 10 * 1000, title, chosenTime, currentUser.getUsername(), matchId);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to create match", e);
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }
}