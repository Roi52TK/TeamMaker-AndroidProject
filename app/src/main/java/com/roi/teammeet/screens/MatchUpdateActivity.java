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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.MyApplication;
import com.roi.teammeet.R;
import com.roi.teammeet.adapters.MyMatchGroupAdapter;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.Range;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.ActivityCollector;
import com.roi.teammeet.utils.DateUtil;
import com.roi.teammeet.utils.MatchValidator;

import java.util.ArrayList;
import java.util.List;

public class MatchUpdateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MatchUpdateActivity";

    EditText etTitle;
    EditText etDescription;
    EditText etDate, etTime, etLocation;
    double lang, lat;
    String address;
    EditText etMinAge;
    EditText etMaxAge;
    EditText etSize;
    String chosenDate;
    String chosenTime;
    Button btnUpdate;
    boolean isDatePicked;
    private DatabaseService databaseService;
    private User hostUser;
    private String ogMatchId;
    private Match ogMatch;
    private TextView tvHost;
    private RecyclerView rvPlayers;
    List<User> players;
    private MyMatchGroupAdapter myMatchGroupAdapter;
    ValueEventListener userListRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ogMatchId = getIntent().getStringExtra("matchId");

        databaseService = DatabaseService.getInstance();

        databaseService.getMatch(ogMatchId, new DatabaseService.DatabaseCallback<Match>() {
            @Override
            public void onCompleted(Match object) {
                Log.d(TAG, "onCompleted: Match received successfully");
                ogMatch = object;
                initData();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive match", e);
            }
        });

        initViews();
        etDate.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etLocation.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);


    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle_matchUpdate);
        etDescription = findViewById(R.id.etDescription_matchUpdate);
        etDate = findViewById(R.id.etDate_matchUpdate);
        etTime = findViewById(R.id.etTime_matchUpdate);
        etLocation = findViewById(R.id.etLocation_matchUpdate);
        etMinAge = findViewById(R.id.etMinAge_matchUpdate);
        etMaxAge = findViewById(R.id.etMaxAge_matchUpdate);
        etSize = findViewById(R.id.etSize_matchUpdate);
        btnUpdate = findViewById(R.id.btnCreate_matchUpdate);

        tvHost = findViewById(R.id.tvHost_matchUpdate);

        rvPlayers = findViewById(R.id.rvPlayers_matchUpdate);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        etTitle.setText(ogMatch.getTitle());
        etDescription.setText(ogMatch.getDescription());

        chosenDate = ogMatch.getDate();
        etDate.setText(chosenDate);
        chosenTime = ogMatch.getTime();
        etTime.setText(chosenTime);

        lang = ogMatch.getLang();
        lat = ogMatch.getLat();

        address = ogMatch.getAddress();
        etLocation.setText(address);

        etMinAge.setText(String.valueOf(ogMatch.getAgeRange().getMin()));
        etMaxAge.setText(String.valueOf(ogMatch.getAgeRange().getMax()));

        etSize.setText(String.valueOf(ogMatch.getSize()));

        databaseService.getUser(ogMatch.getHostUserId(), new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User object) {
                Log.d(TAG, "onCompleted: User received successfully");
                hostUser = object;

                tvHost.setText(tvHost.getText().toString() + hostUser.getUsername());
                tvHost.setOnClickListener(MatchUpdateActivity.this);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive user", e);
            }
        });

        userListRealtime = databaseService.getUserListRealtime(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> userList) {
                Log.d(TAG, "onCompleted: Users received successfully");
                players = new ArrayList<>();

                for (User user : userList){
                    if(ogMatch.hasJoined(user)){
                        if(!ogMatch.isHost(user))
                            players.add(user);
                    }
                }

                myMatchGroupAdapter = new MyMatchGroupAdapter(MatchUpdateActivity.this, ogMatch, players);
                rvPlayers.setAdapter(myMatchGroupAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive users", e);
            }
        });
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
                MatchUpdateActivity.this,
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
                MatchUpdateActivity.this,
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

    private boolean checkInput(String title, String description, String date, String time, String min, String max, String size, int ogGroupCurrent){
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
            int minAge = MyApplication.AGE_LIMIT;
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

        if(!MatchValidator.isUserAgeValid(min, max, hostUser.getBirthDate())){
            Log.e(TAG, "checkInput: Host's age is not in range");
            etMaxAge.setError("Host's age is not in range");
            etMaxAge.requestFocus();
            return false;
        }

        if(!MatchValidator.isSizeValid(size)){
            Log.e(TAG, "checkInput: Size must be between 2-99");
            etSize.setError("Size must be between 2-99");
            etSize.requestFocus();
            return false;
        }

        if(Integer.parseInt(size) < ogGroupCurrent){
            Log.e(TAG, "checkInput: New size must not be less than current group size");
            etSize.setError("New size must not be less than current group size");
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
        else if(v == btnUpdate){
            onClickUpdate();
        }
        else if(v == tvHost){
            Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
            userProfileIntent.putExtra("userId", hostUser.getId());
            startActivity(userProfileIntent);
        }
    }

    private void onClickUpdate() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String min = etMinAge.getText().toString();
        String max = etMaxAge.getText().toString();
        String size = etSize.getText().toString();


        if(!checkInput(title, description, chosenDate, chosenTime, min, max, size, ogMatch.getGroup().getCurrent())){
            return;
        }

        int minNum = Integer.parseInt(min);
        int maxNum = Integer.parseInt(max);
        int sizeNum = Integer.parseInt(size);

        updateMatch(title, description, minNum, maxNum, sizeNum);

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void updateMatch(String title, String description, int min, int max, int size) {
        ogMatch.setTitle(title);
        ogMatch.setDescription(description);
        ogMatch.setTime(chosenTime);
        ogMatch.setLat(lat);
        ogMatch.setLang(lang);
        ogMatch.setAddress(address);
        ogMatch.setAgeRange(new Range(min, max));
        ogMatch.getGroup().setMax(size);

        databaseService.createNewMatch(ogMatch, new DatabaseService.DatabaseCallback<Object>() {
            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: Updated a match");
                Toast.makeText(MatchUpdateActivity.this, "Updated the match!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {

                Log.e(TAG, "onFailed: Failed to update match", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseService.stopListenUserRealtime(this.userListRealtime);
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}