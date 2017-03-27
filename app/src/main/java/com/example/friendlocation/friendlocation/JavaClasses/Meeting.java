package com.example.friendlocation.friendlocation.JavaClasses;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
public class Meeting {
    String fbtoken;
    String user_id;
    String name;
    String place_name;
    Timestamp timestamp;
    double lat;
    double lng;
    List<MeetingAttender> attendersList;

    public Meeting(String fbtoken, String user_id, String name, String place_name, Timestamp timestap, double lat, double lng, List<MeetingAttender> attendersList) {
        this.fbtoken = fbtoken;
        this.user_id = user_id;
        this.name = name;
        this.place_name = place_name;
        this.timestamp = timestap;
        this.lat = lat;
        this.lng = lng;
        this.attendersList = attendersList;
    }

    public Meeting() {
    }

    public String getAttendersStringList() {
        /*String stringList = "";
        for (MeetingAttender attender:
             attendersList) {
            stringList += "," + attender.getUserId();
        }*/

        return TextUtils.join(",", attendersList);
    }
    public List<MeetingAttender> getAttendersList() {
        return attendersList;
    }

    public void addAttendeeToList(MeetingAttender attender) {
        if(attendersList == null)
            attendersList = new ArrayList<>();
        attendersList.add(attender);
    }

    public void setAttendersList(List<MeetingAttender> attendersList) {
        this.attendersList = attendersList;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
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

    public String getFbtoken() {
        return fbtoken;
    }

    public void setFbtoken(String fbtoken) {
        this.fbtoken = fbtoken;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
