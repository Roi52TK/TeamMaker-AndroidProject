package com.roi.teammeet.models;

public class User {
    String id;
    String username, birthYear, gender, phone, email, password;

    public User(){

    }

    public User(String id, String username, String birthYear, String gender, String phone, String email, String password) {
        this.id = id;
        this.username = username;
        this.birthYear = birthYear;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User(String id, String username, String birthYear) {
        this.id = id;
        this.username = username;
        this.birthYear = birthYear;
        this.gender = "";
        this.phone = "";
        this.email = "";
        this.password = "";
    }


    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
