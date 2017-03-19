package com.example.friendlocation.friendlocation.JavaClasses;

import java.util.Timer;

/**
 * Created by barte_000 on 19.03.2017.
 */

public class Meeting {

    int user_id;
    String name;
    double lat;
    double lng;
    Timer time;

    public Meeting(int user_id, String name, double lat, double lng, Timer time) {
        this.user_id = user_id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public int getUser_id() {

        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Timer getTime() {
        return time;
    }

    public void setTime(Timer time) {
        this.time = time;
    }
}
