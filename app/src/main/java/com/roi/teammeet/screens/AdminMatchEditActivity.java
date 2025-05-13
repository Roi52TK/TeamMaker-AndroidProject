package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.MatchEditAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminMatchEditActivity extends BaseActivity {

    private static final String TAG = "AdminMatchEditActivity";

    private DatabaseService databaseService;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MatchEditAdapter matchAdapter;
    private List<Match> matchList;

    ValueEventListener matchListRealtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_match_edit);

        databaseService = DatabaseService.getInstance();

        searchView = findViewById(R.id.sv_adminMatchEdit);
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

        recyclerView = findViewById(R.id.rv_adminMatchEdit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        matchList = new ArrayList<>();
        matchListRealtime = databaseService.getMatchListRealtime(new DatabaseService.DatabaseCallback<List<Match>>() {
            @Override
            public void onCompleted(List<Match> object) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                matchList = object;
                matchAdapter = new MatchEditAdapter(AdminMatchEditActivity.this, matchList);
                recyclerView.setAdapter(matchAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive users", e);
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
        databaseService.stopListenUserRealtime(this.matchListRealtime);
        super.onDestroy();
    }
}