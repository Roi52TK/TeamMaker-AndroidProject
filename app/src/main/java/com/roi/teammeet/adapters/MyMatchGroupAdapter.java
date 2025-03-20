package com.roi.teammeet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.screens.UserProfileActivity;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class MyMatchGroupAdapter extends RecyclerView.Adapter<MyMatchGroupAdapter.MyMatchGroupViewHolder> {

    private static final String TAG = "MyMatchGroupAdapter";
    private Context context;
    private Match match;
    private List<User> userList;

    public MyMatchGroupAdapter(Context context, Match match, List<User> userList) {
        this.context = context;
        this.match = match;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyMatchGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_match_group_item, parent, false);
        return new MyMatchGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMatchGroupViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());

        holder.tvUsername.setOnClickListener(v -> {
            Intent userProfileIntent = new Intent(context, UserProfileActivity.class);
            userProfileIntent.putExtra("userId", user.getId());
            context.startActivity(userProfileIntent);
        });

        holder.btnKick.setOnClickListener(v -> {
            int pos = match.kick(user);
            if (pos == -1) {
                return;
            }
            DatabaseService.getInstance().createNewMatch(match, new DatabaseService.DatabaseCallback<Object>() {
                @Override
                public void onCompleted(Object object) {
                    Toast.makeText(context, "Kicked " + user.getUsername() + " from the match!", Toast.LENGTH_SHORT).show();
                    int position = userList.indexOf(user);
                    userList.remove(user);
                    notifyItemRemoved(position);
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyMatchGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        Button btnKick;

        public MyMatchGroupViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername_myMatchGroupItem);
            btnKick = itemView.findViewById(R.id.btnKick_myMatchGroupItem);
        }
    }
}

