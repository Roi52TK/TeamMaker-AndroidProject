package com.roi.teammeet.models;

import java.time.Year;
import java.util.ArrayList;

public class Match {
    protected String id;
    protected String title;
    protected String details;
    private User creatorUser;
    private String date;
    protected double lang,lat;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<User> players;

    public Match(){

    }

    public Match(String id, String title, String details, User creatorUser, String date, double lang, double lat, int minAge, int maxAge, int groupSize){
        this.id = id;
        this.title = title;
        this.details = details;
        this.creatorUser = creatorUser;
        this.date = date;
        this.lang = lang;
        this.lat = lat;
        this.ageRange = new Range(minAge, maxAge);
        this.groupSize = new GroupSize(groupSize);
        this.players = new ArrayList<>();
        Add(this.creatorUser);
    }

    public void Add(User player){
        if(canJoin(player)){
            this.players.add(player);
            this.groupSize.add();
        }
    }

    public void Remove(User player){
        this.players.remove(player);
        this.groupSize.remove();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", creatorPlayer=" + creatorUser +
                ", date=" + date +
                ", lang=" + lang +
                ", lat=" + lat +
                ", ageRange=" + ageRange +
                ", groupSize=" + groupSize +
                ", players=" + players +
                '}';
    }
}
