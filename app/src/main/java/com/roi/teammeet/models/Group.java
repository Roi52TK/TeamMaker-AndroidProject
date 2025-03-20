package com.roi.teammeet.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private int max;
    private ArrayList<String> playersId;

    public Group() {
        this.max = 0;
        playersId = new ArrayList<>();
    }

    public Group(int max){
        this.max = max;
        this.playersId = new ArrayList<>();
    }

    public void add(String userId){
        if(!isFull()){
            this.playersId.add(userId);
        }
    }

    public int remove(String userId){
        if(!playersId.contains(userId)){
            return -1;
        }
        int pos = playersId.indexOf(userId);
        playersId.remove(userId);
        return pos;
    }

    public boolean isFull(){
        return this.playersId.size() == this.max;
    }

    public boolean contains(String userId){
        return this.playersId.contains(userId);
    }

    public int getCurrent(){
        return this.playersId.size();
    }

    public int getMax(){
        return this.max;
    }

    public void setMax(int max){
        this.max = Math.max(this.getCurrent(), max);
    }

    public ArrayList<String> getPlayersId() {
        return this.playersId;
    }

    public void setPlayersId(ArrayList<String> playersId) {
        this.playersId = playersId;
    }

    @NonNull
    public String toString(){
        return this.playersId.size() + "/" + this.max;
    }
}
