package com.example.friendlocation.friendlocation.JavaClasses;

import java.util.ArrayList;
import java.util.List;
public class Meeting {
    String user_id;
    String name;
    double lat;
    double lng;
    String time;
    List<MeetingAttender> attendersList;

    public Meeting(String user_id, String name, double lat, double lng, String time, List<MeetingAttender> attendersList) {
        this.user_id = user_id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.attendersList = attendersList;
    }

    public Meeting() {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
