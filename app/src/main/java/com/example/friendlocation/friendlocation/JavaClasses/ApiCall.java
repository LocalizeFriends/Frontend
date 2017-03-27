package com.example.friendlocation.friendlocation.JavaClasses;

public class ApiCall {
    String fbtoken;
    double lng;
    double lat;

    public ApiCall(String token, double longitude, double latitude) {
        this.fbtoken = token;
        this.lng = longitude;
        this.lat = latitude;
    }

    public String getFbtoken() {
        return fbtoken;
    }

    public void setFbtoken(String fbtoken) {
        this.fbtoken = fbtoken;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


}
