package com.roi.teammeet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.roi.teammeet.screens.MainActivity;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class JoinedMatchAdapter extends RecyclerView.Adapter<JoinedMatchAdapter.JoinedMatchViewHolder> {

    private Context context;
    private List<Match> matchList;

    public JoinedMatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public JoinedMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joined_match_card, parent, false);
        return new JoinedMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedMatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.tvTitle.setText(match.getTitle());
        holder.tvDescription.setText(match.getDescription());
        holder.tvDate.setText(match.getDate() + " at " + match.getTime());
        holder.tvGroupSize.setText(match.getGroupSize().toString());

        // Handle button click to show the dialog
        holder.btnDetails.setOnClickListener(v -> {
            // Create and show the dialog with more match details
            showDetailsDialog(match);
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class JoinedMatchViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
        TextView tvGroupSize;
        Button btnDetails;
        Button btnLeave;

        public JoinedMatchViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_joinedMatchCard);
            tvDescription = itemView.findViewById(R.id.tvDescription_joinedMatchCard);
            tvDate = itemView.findViewById(R.id.tvDate_joinedMatchCard);
            tvGroupSize = itemView.findViewById(R.id.tvGroupSize_joinedMatchCard);
            btnDetails = itemView.findViewById(R.id.btnDetails_joinedMatchCard);
            btnLeave = itemView.findViewById(R.id.btnLeave_joinedMatchCard);
        }
    }

    // Method to show the custom dialog with match details
    private void showDetailsDialog(Match match) {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(match.getTitle())
                .setMessage("Description: " + match.getDescription() +
                        "\nDate: " + match.getDate() + " at " + match.getTime() +
                        "\nAddress: " + match.getAddress() +
                        "\nAge Range: " + match.getAgeRange() +
                        "\nGroup Size: " + match.getGroupSize())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}

