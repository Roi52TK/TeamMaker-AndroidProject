package com.roi.teammeet.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.roi.teammeet.R;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.adapters.UserEditAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminUserEditActivity extends BaseActivity {

    private static final String TAG = "AdminUserEditActivity";

    private DatabaseService databaseService;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private UserEditAdapter userAdapter;
    private List<User> userList;

    ValueEventListener userListRealtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_edit);

        databaseService = DatabaseService.getInstance();

        searchView = findViewById(R.id.sv_adminUserEdit);
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

        recyclerView = findViewById(R.id.rv_adminUserEdit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userListRealtime = databaseService.getUserListRealtime(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> object) {
                Log.d(TAG, "onCompleted: Matches received successfully");
                userList = object;
                userAdapter = new UserEditAdapter(AdminUserEditActivity.this, userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to receive users", e);
            }
        });
    }

    private void filterList(String text){
        List<User> filteredList = new ArrayList<>();
        for(User user : userList){
            if(user.getUsername().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(user);
            }
        }

        if(!filteredList.isEmpty()){
            userAdapter.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onDestroy() {
        databaseService.stopListenUserRealtime(this.userListRealtime);
        super.onDestroy();
    }
}