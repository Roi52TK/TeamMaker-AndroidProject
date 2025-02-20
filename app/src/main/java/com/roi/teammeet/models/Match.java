package com.roi.teammeet.models;

import java.util.ArrayList;

public class Match {
    protected String id;
    protected String title;
    protected String description;
    private String hostUserId;
    private String date;
    private String time;
    protected double lat, lang;
    private String address;
    private Range ageRange;
    private GroupSize groupSize;
    private ArrayList<String> playersId;

    public Match(){

    }

    public Match(String id, String title, String description, String hostUserId, String date, String time, double lat, double lang, String address, int minAge, int maxAge, int groupSize){
        this.id = id;
        this.title = title;
        this.description = description;
        this.hostUserId = hostUserId;
        this.date = date;
        this.time = time;
        this.lat = lat;
        this.lang = lang;
        this.address = address;
        this.ageRange = new Range(minAge, maxAge);
        this.groupSize = new GroupSize(groupSize);
        this.playersId = new ArrayList<>();
        add(this.hostUserId);
    }

    public boolean join(User user){
        if(canJoin(user)){
            add(user.getId());
            return true;
        }
        return false;
    }

    private void add(String userId){
        this.playersId.add(userId);
        this.groupSize.add();
    }

    public void leave(User user){
        this.playersId.remove(user.getId());
        this.groupSize.remove();
    }

    private boolean canJoin(User user){
        return !hasJoined(user) && ageRange.isInRange(user.getCalculatedAge()) && !groupSize.isFull();
    }

    public boolean hasJoined(User user){
        return playersId.contains(user.getId());
    }

    public boolean isHost(User user){
        return user.getId().equals(hostUserId);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
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
                ", description='" + description + '\'' +
                ", hostUserId='" + hostUserId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", lat=" + lat +
                ", lang=" + lang +
                ", address='" + address + '\'' +
                ", ageRange=" + ageRange +
                ", groupSize=" + groupSize +
                ", playersId=" + playersId +
                '}';
    }
}