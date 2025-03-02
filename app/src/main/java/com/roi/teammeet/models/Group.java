package com.roi.teammeet.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private final int max;
    private int current;
    private ArrayList<String> playersId;

    public Group() {
        this.max = 0;
        this.current = 0;
        playersId = new ArrayList<>();
    }

    public Group(int max){
        this.max = max;
        this.current = 0;
        this.playersId = new ArrayList<>();
    }

    public void add(String userId){
        if(!isFull()){
            this.playersId.add(userId);
            this.current += 1;
        }
    }

    public void remove(String userId){
        if(this.current > 0){
            if(playersId.contains(userId)){
                playersId.remove(userId);
                this.current -= 1;
            }
        }
    }

    public boolean isFull(){
        return this.current == this.max;
    }

    public boolean contains(String userId){
        return this.playersId.contains(userId);
    }

    public int getCurrent(){
        return this.current;
    }

    public int getMax(){
        return this.max;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    @NonNull
    public String toString(){
        return this.current + "/" + this.max;
    }
}
