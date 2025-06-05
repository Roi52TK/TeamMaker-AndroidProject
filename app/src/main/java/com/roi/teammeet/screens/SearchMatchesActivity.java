package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.MatchAdapter;
import com.roi.teammeet.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

public class SearchMatchesActivity extends BaseActivity {

    private static final String TAG = "AvailableMatchesActivity";

    private DatabaseService databaseService;
    private SearchView searchView;
    private RecyclerView rvMatches;
    private TextView tvEmptyMatches;
    private MatchAdapter matchAdapter;
    private List<Match> matchList;

    ValueEventListener matchListRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_search_matches);

        databaseService = DatabaseService.getInstance();

        searchView = findViewById(R.id.sv_searchMatches);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        tvEmptyMatches = findViewById(R.id.tvEmptyMatches_searchMatches);

        rvMatches = findViewById(R.id.rv_searchMatches);
        rvMatches.setLayoutManager(new LinearLayoutManager(this));

        matchList = new ArrayList<>();
        matchListRealtime = databaseService.getMatchListRealtime(new DatabaseService.DatabaseCallback<List<Match>>() {
            @Override
            public void onCompleted(List<Match> matches) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                matchList = matches;

                if(matchList.isEmpty()){
                    tvEmptyMatches.setVisibility(View.VISIBLE);
                    rvMatches.setVisibility(View.GONE);
                }
                else{
                    rvMatches.setVisibility(View.VISIBLE);
                    tvEmptyMatches.setVisibility(View.GONE);

                    matchAdapter = new MatchAdapter(SearchMatchesActivity.this, matchList);
                    rvMatches.setAdapter(matchAdapter);
                }

            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive matches", e);
            }
        });
    }

    private void filterList(String text){
        List<Match> filteredList = new ArrayList<>();
        for(Match match : matchList){
            if(match.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(match);
            }
        }

        if(!filteredList.isEmpty()){
            matchAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onDestroy() {
        databaseService.stopListenMatchRealtime(this.matchListRealtime);
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}