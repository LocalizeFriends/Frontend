package com.example.friendlocation.friendlocation.JavaClasses.Model;


import java.sql.Timestamp;

public class Location {
    double lat;
    double lng;
    Timestamp timestap;

    public Location(double lat, double lng, Timestamp timestap) {
        this.lat = lat;
        this.lng = lng;
        this.timestap = timestap;
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

    public Timestamp getTimestap() {
        return timestap;
    }

    public void setTimestap(Timestamp timestap) {
        this.timestap = timestap;
    }
}

