package com.procus.simpleuserregistration.models;

import android.graphics.Bitmap;

/**
 * Created by Peter on 2.7.17.
 */

public class User {
    private String registerTime;
    private String birthday;
    private String name;
    private String surname;
    private Double longitude;
    private Double latitude;
    private String photo;
    private Integer id;
    private String devId;

    public User() {

    }

    public User(String registerTime, String birthday, String name, String surname, Double longitude, Double latitude, String photo, String devId) {
        this.registerTime = registerTime;
        this.birthday = birthday;
        this.name = name;
        this.surname = surname;
        this.longitude = longitude;
        this.latitude = latitude;
        this.photo = photo;
        this.devId = devId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }
}
