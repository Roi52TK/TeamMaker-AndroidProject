package com.roi.teammeet.screens;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.utils.MatchAdapter;

import java.util.ArrayList;
import java.util.List;

public class AvailableMatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private List<Match> matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO: receive data from firebase
        matchList = new ArrayList<>();
        matchList.add(new Match("1", "Football Match", "A fun match with friends.", "user1", "2025-01-01", "15:00", 40.7128, -74.0060, "Central Park", 18, 30, 10));
        matchList.add(new Match("2", "Basketball Game", "Looking for more players.", "user2", "2025-01-02", "18:00", 34.0522, -118.2437, "LA Park", 20, 40, 8));

        matchAdapter = new MatchAdapter(this, matchList);
        recyclerView.setAdapter(matchAdapter);
    }
}