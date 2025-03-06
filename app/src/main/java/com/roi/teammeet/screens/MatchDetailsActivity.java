package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.MatchGroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class MatchDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MatchDetailsActivity";

    Match match;
    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    TextView tvTime;
    TextView tvAddress;
    TextView tvAgeRange;
    TextView tvHost;

    RecyclerView rvPlayers;
    MatchGroupAdapter matchGroupAdapter;
    List<String> usersId;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_match_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        String matchId = getIntent().getStringExtra("matchId");
        DatabaseService.getInstance().getMatch(matchId, new DatabaseService.DatabaseCallback<Match>() {
            @Override
            public void onCompleted(Match object) {
                Log.d(TAG, "onCompleted: Match received successfully");
                match = object;
                usersId = match.getGroup().getPlayersId();
                initData();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive match", e);
            }
        });
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle_matchDetails);
        tvDescription = findViewById(R.id.tvDescription_matchDetails);
        tvDate = findViewById(R.id.tvDate_matchDetails);
        tvTime = findViewById(R.id.tvTime_matchDetails);
        tvAddress = findViewById(R.id.tvAddress_matchDetails);
        tvAgeRange = findViewById(R.id.tvAgeRange_matchDetails);
        tvHost = findViewById(R.id.tvHost_matchDetails);

        rvPlayers = findViewById(R.id.rvPlayers_matchDetails);
        rvPlayers.setLayoutManager(new LinearLayoutManager(this));

        usersId = new ArrayList<>();
        users = new ArrayList<>();
    }

    private void initData() {
        tvTitle.setText(match.getTitle().toString());
        tvDescription.setText(match.getDescription().toString());
        tvDate.setText("Date: " + match.getDate().toString());
        tvTime.setText("Time: " + match.getTime().toString());
        tvAddress.setText("Address: " + match.getAddress().toString());
        tvAgeRange.setText("Age Range: "+ match.getAgeRange().toString());

        DatabaseService.getInstance().getUser(match.getHostUserId(), new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                Log.d(TAG, "onCompleted: Host user received successfully");
                tvHost.setText("Host: " + user.getUsername());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive host user", e);
            }
        });

        DatabaseService.getInstance().getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> userList) {
                Log.d(TAG, "onCompleted: Users received successfully");

                for (User user : userList){
                    if(match.hasJoined(user)){
                        users.add(user);
                    }
                }

                matchGroupAdapter = new MatchGroupAdapter(MatchDetailsActivity.this, users);
                rvPlayers.setAdapter(matchGroupAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive users", e);
            }
        });
    }
}