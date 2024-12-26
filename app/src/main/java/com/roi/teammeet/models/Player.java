package com.roi.teammeet.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int age;
    private String gender;

    public Player(String name, int age, String gender){
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    @NonNull
    public String toString(){
        return this.name + ", " + this.age + ", " + this.gender;
    }
}
