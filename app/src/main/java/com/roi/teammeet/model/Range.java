package com.roi.teammeet.model;

import androidx.annotation.NonNull;

public class Range {
    int min;
    int max;

    public Range(int min, int max){
        this.min = Math.max(0, min);
        this.max = Math.max(0, max);
    }

    public boolean isInRange(int x){
        return x >= min && x <= max;
    }

    @NonNull
    public String toString(){
        return this.min + "-" + this.max;
    }
}
