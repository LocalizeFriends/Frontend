package com.example.friendlocation.friendlocation.JavaClasses;


import com.google.gson.annotations.SerializedName;

public class FriendLocation {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("Location")
    Location Location;

    public FriendLocation(String id, String name, Location Location) {
        this.id = id;
        this.name = name;
        this.Location = Location;
    }
}
