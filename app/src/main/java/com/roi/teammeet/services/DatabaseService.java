package com.roi.teammeet.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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

    /// remove data from the database at a specific path
    /// @param path the path to remove the data from
    /// @param callback the callback to call when the operation is completed
    /// @see DatabaseCallback
    private void deleteData(@NotNull final String path, @Nullable final DatabaseCallback<Void> callback) {
        databaseReference.child(path).removeValue().addOnCompleteListener(task -> {
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

        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });
    }


    private <T> ValueEventListener getDataListListener(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        return readData(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> tList = new ArrayList<>();
                snapshot.getChildren().forEach(dataSnapshot -> {
                    T t = dataSnapshot.getValue(clazz);
                    tList.add(t);
                });
                callback.onCompleted(tList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }

    private void stopListen(@NotNull final String path, @NotNull final ValueEventListener listener) {
        readData(path).removeEventListener(listener);
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

    public ValueEventListener getMatchListRealtime(@NotNull final DatabaseCallback<List<Match>> callback) {
        return getDataListListener("matches/", Match.class, callback);
    }

    public void stopListenMatchRealtime(ValueEventListener listener) {
        stopListen("matches/", listener);
    }

    public void deleteMatch(@NotNull final String matchId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("matches/" + matchId, callback);
    }

    public void createNewUser(User user, DatabaseCallback<Object> callback) {
        writeData("users/" + user.getId(), user, callback);
    }

    public void getUser(String userId, DatabaseCallback<User> callback) {
        getData("users/" + userId, User.class, callback);
    }

    public void getUserList(@NotNull final DatabaseCallback<List<User>> callback) {
        getDataList("users/", User.class, new HashMap<>(), callback);
    }

    public ValueEventListener getUserListRealtime(@NotNull final DatabaseCallback<List<User>> callback) {
        return getDataListListener("users/", User.class, callback);
    }

    public void stopListenUserRealtime(ValueEventListener listener) {
        stopListen("users/", listener);
    }

    public void deleteUser(@NotNull final String userId, @Nullable final DatabaseCallback<Void> callback) {
        deleteData("users/" + userId, callback);
    }

    public String generateMatchId() {
        return generateNewId("matches");
    }

}