package com.roi.teammeet.model;

import androidx.annotation.NonNull;

public class GroupSize {
    private int current;
    private final int max;

    public GroupSize(int max){
        this.current = 0;
        this.max = max;
    }

    public void add(){
        if(this.current < this.max){
            this.current += 1;
        }
    }

    public void remove(){
        if(this.current > 0){
            this.current -= 1;
        }
    }

    public boolean isFull(){
        return this.current == this.max;
    }

    public int getCurrent(){
        return this.current;
    }

    public int getMax(){
        return this.max;
    }

    @NonNull
    public String toString(){
        return this.current + "/" + this.max;
    }
}
