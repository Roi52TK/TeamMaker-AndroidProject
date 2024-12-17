package com.roi.teammeet.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int age;
    private Gender gender;

    public Player(String name, int age, Gender gender){
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

    public Gender getGender() {
        return gender;
    }

    @NonNull
    public String toString(){
        return this.name + ", " + this.age + ", " + this.gender;
    }
}
