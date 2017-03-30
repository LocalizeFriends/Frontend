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

    public com.example.friendlocation.friendlocation.JavaClasses.Location getLocation() {
        return Location;
    }

    public void setLocation(com.example.friendlocation.friendlocation.JavaClasses.Location location) {
        Location = location;
    }
}
