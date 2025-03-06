package com.roi.teammeet.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.screens.UserProfileActivity;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class MatchGroupAdapter extends RecyclerView.Adapter<MatchGroupAdapter.MatchGroupViewHolder> {

    private Context context;
    private List<User> userList;

    public MatchGroupAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MatchGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_group_item, parent, false);
        return new MatchGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchGroupViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText(user.getUsername());

        holder.tvUsername.setOnClickListener(v -> {
            Intent userProfileIntent = new Intent(context, UserProfileActivity.class);
            userProfileIntent.putExtra("userId", user.getId());
            context.startActivity(userProfileIntent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MatchGroupViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;

        public MatchGroupViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername_matchGroupItem);
        }
    }
}

