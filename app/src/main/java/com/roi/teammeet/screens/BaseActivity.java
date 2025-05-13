package com.roi.teammeet.screens;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.roi.teammeet.R;
import com.roi.teammeet.services.AuthenticationService;
import com.roi.teammeet.utils.SharedPreferencesUtil;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent goIntent;

        if(item.getItemId() == R.id.home_menu){
            goIntent = new Intent(this, MainActivity.class);
            startActivity(goIntent);
            finish();
        }
        else if(item.getItemId() == R.id.searchMatches_menu){
            goIntent = new Intent(this, SearchMatchesActivity.class);
            startActivity(goIntent);
            finish();
        }
        else if(item.getItemId() == R.id.myMatches_menu){
            goIntent = new Intent(this, MyMatchesActivity.class);
            startActivity(goIntent);
            finish();
        }
        else if(item.getItemId() == R.id.myProfile_menu){
            goIntent = new Intent(this, MyProfileActivity.class);
            startActivity(goIntent);
            finish();
        }
        else if(item.getItemId() == R.id.logout_menu){
            SignOut();
            goIntent = new Intent(this, LandingActivity.class);
            startActivity(goIntent);
            finish();
        }

        return  true;
    }

    protected void SignOut(){
        AuthenticationService.getInstance().signOut();
        SharedPreferencesUtil.signOutUser(this);
    }
}