package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.MyMatchGroupAdapter;
import com.roi.teammeet.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class MyMatchDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MyMatchDetailsActivity";

    Match match;
    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    TextView tvTime;
    TextView tvAddress;
    TextView tvAgeRange;
    TextView tvGroup;
    TextView tvHost;

    RecyclerView rvPlayers;
    MyMatchGroupAdapter myMatchGroupAdapter;
    List<User> players;
    DatabaseService databaseService;
    ValueEventListener userListRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_match_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        initViews();

        String matchId = getIntent().getStringExtra("matchId");
        databaseService.getMatch(matchId, new DatabaseService.DatabaseCallback<Match>() {
            @Override
            public void onCompleted(Match object) {
                Log.d(TAG, "onCompleted: Match received successfully");
                match = object;
                initData();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive match", e);
            }
        });
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle_myMatchDetails);
        tvDescription = findViewById(R.id.tvDescription_myMatchDetails);
        tvDate = findViewById(R.id.tvDate_myMatchDetails);
        tvTime = findViewById(R.id.tvTime_myMatchDetails);
        tvAddress = findViewById(R.id.tvAddress_myMatchDetails);
        tvAgeRange = findViewById(R.id.tvAgeRange_myMatchDetails);
        tvGroup = findViewById(R.id.tvGroup_myMatchDetails);
        tvHost = findViewById(R.id.tvHost_myMatchDetails);

        rvPlayers = findViewById(R.id.rvPlayers_myMatchDetails);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        tvTitle.setText(match.getTitle().toString());
        tvDescription.setText("תיאור: " + match.getDescription().toString());
        tvDate.setText("תאריך: " + match.getDate().toString());
        tvTime.setText("שעה: " + match.getTime().toString());
        tvAddress.setText("כתובת: " + match.getAddress().toString());
        tvAddress.setOnClickListener(this);
        tvAgeRange.setText("טווח גילים: "+ match.getAgeRange().toString());
        tvGroup.setText("מספר משתתפים: " + match.getGroup().toString());

        userListRealtime = databaseService.getUserListRealtime(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> userList) {
                Log.d(TAG, "onCompleted: Users received successfully");
                players = new ArrayList<>();

                for (User user : userList){
                    if(match.hasJoined(user)){
                        if(!match.isHost(user))
                            players.add(user);
                    }
                }

                myMatchGroupAdapter = new MyMatchGroupAdapter(MyMatchDetailsActivity.this, match, players);
                rvPlayers.setAdapter(myMatchGroupAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive users", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseService.stopListenUserRealtime(this.userListRealtime);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v == tvAddress){
            DialogUtil.openMapOptions(this, match.getLat(), match.getLang());
        }
    }
}