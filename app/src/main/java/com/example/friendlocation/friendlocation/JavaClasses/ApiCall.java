package com.example.friendlocation.friendlocation.JavaClasses;

public class ApiCall {
    String token;
    double longitude;
    double latitude;

    public ApiCall(String token, double longitude, double latitude) {
        this.token = token;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
