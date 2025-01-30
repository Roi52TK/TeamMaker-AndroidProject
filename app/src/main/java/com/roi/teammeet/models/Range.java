package com.roi.teammeet.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Range implements Serializable {
    int min;
    int max;

    public Range() {
    }

    public Range(int min, int max){
        this.min = Math.max(0, min);
        this.max = Math.max(0, max);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isInRange(int x){
        return x >= min && x <= max;
    }

    @NonNull
    public String toString(){
        return this.min + "-" + this.max;
    }
}
