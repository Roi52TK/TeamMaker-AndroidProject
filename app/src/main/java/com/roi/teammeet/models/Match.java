package com.roi.teammeet.models;

import com.roi.teammeet.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Match {
    protected String id;
    protected String name;
    protected String details;
    private Player creatorPlayer;
    private Date date;
    protected String city;
    protected double lang,lat;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<Player> players;
    protected MatchStatus status;

    public Match(){

    }

    public Match(String name, String details, Player creatorPlayer, Date date, String city, int minAge, int maxAge, int groupSize){
        this.name = name;
        this.details = details;
        this.creatorPlayer = creatorPlayer;
        this.date = date;
        this.city = city;
        this.ageRange = new Range(minAge, maxAge);
        this.groupSize = new GroupSize(groupSize);
        this.players = new ArrayList<>();
        Join(this.creatorPlayer);
    }

    public void Join(Player player){
        if(canJoin(player)){
            this.players.add(player);
            this.groupSize.add();
            UpdateStatus();
        }
    }

    public void Leave(Player player){
        this.players.remove(player);
        this.groupSize.remove();
        UpdateStatus();
    }

    private void UpdateStatus(){
        Date currentDate = new Date();

        if(this.date.after(currentDate)){
            this.status = MatchStatus.OutDated;
        }
        else if(this.groupSize.isFull()){
            this.status = MatchStatus.Full;
        }
        else if(this.groupSize.getCurrent() == 1){
            this.status = MatchStatus.Empty;
        }
        else{
            this.status = MatchStatus.Open;
        }
    }

    private boolean canJoin(Player player){
        return ageRange.isInRange(player.getAge()) && !groupSize.isFull();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Player getCreatorPlayer() {
        return creatorPlayer;
    }

    public void setCreatorPlayer(Player creatorPlayer) {
        this.creatorPlayer = creatorPlayer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Range getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Range ageRange) {
        this.ageRange = ageRange;
    }

    public GroupSize getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(GroupSize groupSize) {
        this.groupSize = groupSize;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", creatorPlayer=" + creatorPlayer +
                ", date=" + date +
                ", city='" + city + '\'' +
                ", lang=" + lang +
                ", lat=" + lat +
                ", ageRange=" + ageRange +
                ", groupSize=" + groupSize +
                ", players=" + players +
                ", status=" + status +
                '}';
    }
}
