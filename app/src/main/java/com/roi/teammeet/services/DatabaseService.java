package com.roi.teammeet.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseService {

    private static final String TAG = "DatabaseService";

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    private static DatabaseService instance;
    private final DatabaseReference databaseReference;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void writeData(String path, Object data, @Nullable DatabaseCallback callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(null);
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(String path) {
        return databaseReference.child(path);
    }

    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// get a list of data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the objects to return
    /// @param callback the callback to call when the operation is completed
    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull Map<String, String> filter, @NotNull final DatabaseCallback<List<T>> callback) {
        Query dbRef = readData(path);

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            dbRef = dbRef.orderByChild(entry.getKey()).equalTo(entry.getValue());
        }

        dbRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
        });
    }

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    public void createNewMatch(Match match, DatabaseCallback<Object> callback){
        writeData("matches/" + match.getId(), match, callback);
    }

    public void getMatch(String matchId, DatabaseCallback<Match> callback){
        getData("matches/" + matchId, Match.class, callback);
    }

    public void getMatchList(@NotNull final DatabaseCallback<List<Match>> callback) {
        getDataList("matches/", Match.class, new HashMap<>(), callback);
    }

    public void createNewUser(User user, DatabaseCallback<Object> callback) {
        writeData("users/" + user.getId(), user, callback);
    }

    public void getUser(String userId, DatabaseCallback<User> callback) {
        getData("users/" + userId, User.class, callback);
    }

    public String generateMatchId() {
        return generateNewId("matches");
    }

}