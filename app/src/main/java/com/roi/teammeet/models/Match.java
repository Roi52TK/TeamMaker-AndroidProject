package com.roi.teammeet.models;

import com.roi.teammeet.R;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Match {
    protected String id;
    protected String name;
    protected String details;
    private User creatorUser;
    private Date date;
    protected String city;
    protected double lang,lat;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<User> players;
    protected MatchStatus status;

    public Match(){

    }

    public Match(String id, String name, String details, User creatorUser, Date date, String city, int minAge, int maxAge, int groupSize){
        this.id = id;
        this.name = name;
        this.details = details;
        this.creatorUser = creatorUser;
        this.date = date;
        this.city = city;
        this.ageRange = new Range(minAge, maxAge);
        this.groupSize = new GroupSize(groupSize);
        this.players = new ArrayList<>();
        Join(this.creatorUser);
    }

    public void Join(User player){
        if(canJoin(player)){
            this.players.add(player);
            this.groupSize.add();
            UpdateStatus();
        }
    }

    public void Leave(User player){
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

    private boolean canJoin(User player){
        int age = Year.now().getValue() - Integer.parseInt(player.getBirthYear());
        return ageRange.isInRange(age) && !groupSize.isFull();
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

    public User getCreatorPlayer() {
        return creatorUser;
    }

    public void setCreatorPlayer(User creatorUser) {
        this.creatorUser = creatorUser;
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

    public ArrayList<User> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
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
                ", creatorPlayer=" + creatorUser +
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
