package com.example.friendlocation.friendlocation.JavaClasses.Model;


import com.google.gson.annotations.SerializedName;

public class FriendLocation {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("location")
    Location Location;
    @SerializedName("distance")
    double distance;

    public FriendLocation(String id, String name, Location Location, double distance) {
        this.id = id;
        this.name = name;
        this.Location = Location;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.example.friendlocation.friendlocation.JavaClasses.Model.Location getLocation() {
        return Location;
    }

    public void setLocation(com.example.friendlocation.friendlocation.JavaClasses.Model.Location location) {
        Location = location;
    }

}
