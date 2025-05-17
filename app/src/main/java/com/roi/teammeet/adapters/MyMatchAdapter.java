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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.screens.MyMatchDetailsActivity;
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
        holder.tvDescription.setText("תיאור: " + match.getDescription());
        holder.tvDate.setText("תאריך ושעה: " + match.getDate() + " ב- " + match.getTime());
        holder.tvGroupSize.setText("מספר משתתפים: " + match.getGroup().toString());

        // Handle button click to show the dialog
        holder.btnDetails.setOnClickListener(v -> {
            Intent myMatchDetailsIntent = new Intent(context, MyMatchDetailsActivity.class);
            myMatchDetailsIntent.putExtra("matchId", match.getId());
            context.startActivity(myMatchDetailsIntent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            holder.btnDelete.setEnabled(false);
            DatabaseService.getInstance().deleteMatch(match.getId(), new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void object) {
                    Toast.makeText(context, "Deleted the match!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {

                }
            });
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
}

