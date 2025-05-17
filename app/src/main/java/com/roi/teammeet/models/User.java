package com.roi.teammeet.models;

import com.roi.teammeet.R;
import com.roi.teammeet.utils.DateUtil;

import java.time.Year;
import java.util.Objects;

public class User {
    String id;
    String username, birthDate, gender, phone, email, password;
    boolean isAdmin;

    public User(){

    }

    public User(String id, String username, String birthDate, String gender, String phone, String email, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String id, String username, String birthDate, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = null;
        this.phone = "";
        this.email = "";
        this.password = "";
        this.isAdmin = isAdmin;
    }

    public int getAge(){
        return DateUtil.getAge(birthDate);
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", birthYear='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
