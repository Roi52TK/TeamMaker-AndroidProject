package com.roi.teammeet.models;

import java.util.ArrayList;
import java.util.Date;

public class Match {
    private Player creatorPlayer;
    private Date date;
    private MatchLocation matchLocation;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<Player> players;

    public Match(Player creatorPlayer, Date date, Range ageRange, GroupSize groupSize){
        this.creatorPlayer = creatorPlayer;
        this.date = date;
        this.ageRange = ageRange;
        this.groupSize = groupSize;
        this.players = new ArrayList<>();
    }

    public void Join(Player player){
        if(canJoin(player)){
            players.add(player);
        }
    }

    private boolean canJoin(Player player){
        return ageRange.isInRange(player.getAge()) && !groupSize.isFull();
    }
}
