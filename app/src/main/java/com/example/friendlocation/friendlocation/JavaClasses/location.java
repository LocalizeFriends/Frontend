package com.example.friendlocation.friendlocation.JavaClasses;


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
}

