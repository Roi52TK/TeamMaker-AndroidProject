package com.roi.teammeet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private static final String TAG = "MatchAdapter";
    private Context context;
    private List<Match> matchList;
    private User currentUser;
    private DatabaseService databaseService;

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    public void setFilteredList(List<Match> filteredList){
        this.matchList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.tvTitle.setText(match.getTitle());
        holder.tvDescription.setText(match.getDescription());
        holder.tvDate.setText(match.getDate() + " at " + match.getTime());
        holder.tvGroupSize.setText(match.getGroup().toString());

        databaseService = DatabaseService.getInstance();

        // Handle button click to show the dialog
        holder.btnDetails.setOnClickListener(v -> {
            // Create and show the dialog with more match details
            showDetailsDialog(match);
        });

        holder.btnJoin.setOnClickListener(v -> {
            currentUser = SharedPreferencesUtil.getUser(context);
            if(match.join(currentUser)){
                databaseService.getMatch(match.getId(), new DatabaseService.DatabaseCallback<Match>() {
                    @Override
                    public void onCompleted(Match object) {
                        if(object == null){
                            Toast.makeText(context, "Match has already got deleted.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            databaseService.createNewMatch(match, new DatabaseService.DatabaseCallback<Object>() {
                                @Override
                                public void onCompleted(Object object) {
                                    Toast.makeText(context, "Joined the match!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(Exception e) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }
            else{
                Toast.makeText(context, "Could not join the match!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
        TextView tvGroupSize;
        Button btnDetails;
        Button btnJoin;

        public MatchViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_itemCard);
            tvDescription = itemView.findViewById(R.id.tvDescription_itemCard);
            tvDate = itemView.findViewById(R.id.tvDate_itemCard);
            tvGroupSize = itemView.findViewById(R.id.tvGroupSize_itemCard);
            btnDetails = itemView.findViewById(R.id.btnDetails_itemCard);
            btnJoin = itemView.findViewById(R.id.btnJoin_itemCard);
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
                        "\nGroup Size: " + match.getGroup())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}

