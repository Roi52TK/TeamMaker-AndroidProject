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

public class MyMatchAdapter extends RecyclerView.Adapter<MyMatchAdapter.MyMatchViewHolder> {

    private Context context;
    private List<Match> matchList;

    public MyMatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public MyMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_match_card, parent, false);
        return new MyMatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMatchViewHolder holder, int position) {
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

    public static class MyMatchViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
        TextView tvGroupSize;
        Button btnDetails;
        Button btnDelete;

        public MyMatchViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_myMatchCard);
            tvDescription = itemView.findViewById(R.id.tvDescription_myMatchCard);
            tvDate = itemView.findViewById(R.id.tvDate_myMatchCard);
            tvGroupSize = itemView.findViewById(R.id.tvGroupSize_myMatchCard);
            btnDetails = itemView.findViewById(R.id.btnDetails_myMatchCard);
            btnDelete = itemView.findViewById(R.id.btnDelete_myMatchCard);
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

