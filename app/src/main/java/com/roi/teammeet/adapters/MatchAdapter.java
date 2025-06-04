package com.roi.teammeet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.roi.teammeet.R;
import com.roi.teammeet.models.Match;
import com.roi.teammeet.models.User;
import com.roi.teammeet.screens.MatchDetailsActivity;
import com.roi.teammeet.services.DatabaseService;
import com.roi.teammeet.utils.MatchNotificationWorker;
import com.roi.teammeet.utils.ReminderUtils;
import com.roi.teammeet.utils.SharedPreferencesUtil;

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
        holder.tvDescription.setText("תיאור: " + match.getDescription());
        holder.tvDate.setText("תאריך ושעה: " + match.getDate() + " ב- " + match.getTime());
        holder.tvGroupSize.setText("מספר משתתפים: " + match.getGroup().toString());

        databaseService = DatabaseService.getInstance();

        // Handle button click to show the dialog
        holder.btnDetails.setOnClickListener(v -> {
            Intent matchDetailsIntent = new Intent(context, MatchDetailsActivity.class);
            matchDetailsIntent.putExtra("matchId", match.getId());
            context.startActivity(matchDetailsIntent);
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

                                    //Immediate Notification
                                    MatchNotificationWorker.joinedMatchNotification(context, match.getTitle(), match.getDate(), match.getTime(), match.getId());

                                    //Timed Notification
                                    long meetingTimeMillis = ReminderUtils.dateTimeToMilliseconds(match.getDate(), match.getTime());
                                    ReminderUtils.scheduleMeetingReminder(context, meetingTimeMillis, match.getTitle(), match.getTime(), match.getId());
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
}

