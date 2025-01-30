package com.roi.teammeet.models;

import com.roi.teammeet.services.DatabaseService;

import java.util.ArrayList;

public class Match {
    protected String id;
    protected String title;
    protected String details;
    private String creatorUserId;
    private String date;
    private String time;
    protected double lang,lat;
    private String address;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<String> playersId;

    public Match(){

    }

    public Match(String id, String title, String details, String creatorUserId, String date, String time, double lang, double lat, String address, int minAge, int maxAge, int groupSize){
        this.id = id;
        this.title = title;
        this.details = details;
        this.creatorUserId = creatorUserId;
        this.date = date;
        this.time = time;
        this.lang = lang;
        this.lat = lat;
        this.address = address;
        this.ageRange = new Range(minAge, maxAge);
        this.groupSize = new GroupSize(groupSize);
        this.playersId = new ArrayList<>();
        add(this.creatorUserId);
    }

    public void join(User user){
        if(canJoin(user)){
            add(user.getId());
        }
    }

    private void add(String userId){
        this.playersId.add(userId);
        this.groupSize.add();
    }

    public void remove(String userId){
        this.playersId.remove(userId);
        this.groupSize.remove();
    }

    private boolean canJoin(User user){
        return ageRange.isInRange(user.calculateAge()) && !groupSize.isFull();
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

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ArrayList<String> getPlayersId() {
        return playersId;
    }

    public void setPlayersId(ArrayList<String> playersId) {
        this.playersId = playersId;
    }

    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", creatorUserId='" + creatorUserId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", lang=" + lang +
                ", lat=" + lat +
                ", address='" + address + '\'' +
                ", ageRange=" + ageRange +
                ", groupSize=" + groupSize +
                ", playersId=" + playersId +
                '}';
    }
}