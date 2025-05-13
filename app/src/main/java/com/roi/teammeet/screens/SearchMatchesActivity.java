package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.MatchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchMatchesActivity extends BaseActivity {

    private static final String TAG = "AvailableMatchesActivity";

    private DatabaseService databaseService;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private List<Match> matchList;

    ValueEventListener matchListRealtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        recyclerView = findViewById(R.id.rv_searchMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        matchList = new ArrayList<>();
        matchListRealtime = databaseService.getMatchListRealtime(new DatabaseService.DatabaseCallback<List<Match>>() {
            @Override
            public void onCompleted(List<Match> object) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                matchList = object;
                matchAdapter = new MatchAdapter(SearchMatchesActivity.this, matchList);
                recyclerView.setAdapter(matchAdapter);
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
    }
}