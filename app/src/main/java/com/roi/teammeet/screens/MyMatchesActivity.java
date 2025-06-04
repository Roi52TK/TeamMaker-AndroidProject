package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.JoinedMatchAdapter;
import com.roi.teammeet.adapters.MyMatchAdapter;
import com.roi.teammeet.utils.ActivityCollector;
import com.roi.teammeet.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

public class MyMatchesActivity extends BaseActivity {

    private static final String TAG = "MyMatchesActivity";

    private DatabaseService databaseService;
    private RecyclerView rvMyMatches;
    private RecyclerView rvJoinedMatches;
    private MyMatchAdapter myMatchAdapter;
    private JoinedMatchAdapter joinedMatchAdapter;
    private List<Match> myMatchList;
    private List<Match> joinedMatchList;
    private User currentUser;
    ValueEventListener matchListRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_my_matches);
        databaseService = DatabaseService.getInstance();
        currentUser = SharedPreferencesUtil.getUser(this);

        rvMyMatches = findViewById(R.id.rv_myMatches);
        rvMyMatches.setLayoutManager(new LinearLayoutManager(this));

        rvJoinedMatches = findViewById(R.id.rv_joinedMatches);
        rvJoinedMatches.setLayoutManager(new LinearLayoutManager(this));

        matchListRealtime = databaseService.getMatchListRealtime(new DatabaseService.DatabaseCallback<List<Match>>() {
            @Override
            public void onCompleted(List<Match> object) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                myMatchList = new ArrayList<>();
                joinedMatchList = new ArrayList<>();

                for (Match m: object) {
                    if(m.isHost(currentUser)){
                        myMatchList.add(m);
                    }
                    else if(m.hasJoined(currentUser)){
                        joinedMatchList.add(m);
                    }
                }
                myMatchAdapter = new MyMatchAdapter(MyMatchesActivity.this, myMatchList);
                rvMyMatches.setAdapter(myMatchAdapter);

                joinedMatchAdapter = new JoinedMatchAdapter(MyMatchesActivity.this, joinedMatchList);
                rvJoinedMatches.setAdapter(joinedMatchAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive matches", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseService.stopListenMatchRealtime(this.matchListRealtime);
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}