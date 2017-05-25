package com.example.friendlocation.friendlocation.JavaClasses.Model;

import java.util.Map;

public class Notification {
    String type;
    int meetup_id;
    int organizer_id;
    int user_id;
    boolean new_status;

    public Notification(String type, int meetup_id, int organizer_id) {
        this.type = type;
        this.meetup_id = meetup_id;
        this.organizer_id = organizer_id;
    }

    public Notification(String type, int meetup_id, int user_id, boolean new_status) {
        this.type = type;
        this.meetup_id = meetup_id;
        this.user_id = user_id;
        this.new_status = new_status;
    }

    public Notification(String type, int meetup_id, boolean new_status) {
        this.type = type;
        this.meetup_id = meetup_id;
        this.new_status = new_status;
    }

    public Notification(Map<String, String> map) {
        this.type = map.get("type");
        this.meetup_id = Integer.valueOf(map.get("meetup_id"));
        this.organizer_id = Integer.valueOf(map.get("organizer_id"));
        this.user_id = Integer.valueOf(map.get("user_id"));
        this.new_status = Boolean.valueOf(map.get("new_status"));
        //Change to kotlin it will be better.
        //CheckNull // Not implemented
    }

    String getMeetupName(){
        return null; // Not implemented
    }

    String getOrganizerName(){
        return null; // Not implemented
    }

}
