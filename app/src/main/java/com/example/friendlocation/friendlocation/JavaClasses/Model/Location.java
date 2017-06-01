package com.example.friendlocation.friendlocation.JavaClasses.Model;


import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Location {
    @SerializedName("latitude")
    double lat;
    @SerializedName("longitude")
    double lng;
    @SerializedName("timestamp_ms")
    String timestap;

    public Location(double lat, double lng, String timestap) {
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

    public String getTimestap() {
        return timestap;
    }

    public void setTimestap(String timestap) {
        this.timestap = timestap;
    }
}

