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
import com.roi.teammeet.screens.AdminMatchUpdateActivity;
import com.roi.teammeet.services.DatabaseService;

import java.util.List;

public class MatchEditAdapter extends RecyclerView.Adapter<MatchEditAdapter.MatchEditViewHolder> {

    private static final String TAG = "MatchEditAdapter";
    private Context context;
    private List<Match> matchList;
    private DatabaseService databaseService;

    public MatchEditAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    public void setFilteredList(List<Match> filteredList){
        this.matchList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchEditAdapter.MatchEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_edit_card, parent, false);
        return new MatchEditAdapter.MatchEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchEditAdapter.MatchEditViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.tvTitle.setText(match.getTitle());
        holder.tvDescription.setText(match.getDescription());
        holder.tvDate.setText(match.getDate() + " at " + match.getTime());
        holder.tvGroupSize.setText(match.getGroup().toString());


        holder.btnUpdate.setOnClickListener(v -> {
            Intent updateIntent = new Intent(context, AdminMatchUpdateActivity.class);
            updateIntent.putExtra("matchId", match.getId());
            context.startActivity(updateIntent);
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

    public static class MatchEditViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
        TextView tvGroupSize;
        Button btnUpdate;
        Button btnDelete;

        public MatchEditViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_matchEditCard);
            tvDescription = itemView.findViewById(R.id.tvDescription_matchEditCard);
            tvDate = itemView.findViewById(R.id.tvDate_matchEditCard);
            tvGroupSize = itemView.findViewById(R.id.tvGroupSize_matchEditCard);
            btnUpdate = itemView.findViewById(R.id.btnUpdate_matchEditCard);
            btnDelete = itemView.findViewById(R.id.btnDelete_matchEditCard);
        }
    }
}

