package com.example.friendlocation.friendlocation.JavaClasses.Model;

import com.facebook.AccessToken;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
public class Meeting {
    String fbtoken;
    String user_id;
    String name;
    String place_name;
    @SerializedName("creation_timestamp_ms")
    long timestamp;
    @SerializedName("latitude")
    double lat;
    @SerializedName("longitude")
    double lng;
    List<MeetingAttender> attendersList;

    public Meeting(String user_id, String name, String place_name, long timestap, LatLng latLng, List<MeetingAttender> attendersList) {
        this.fbtoken = AccessToken.getCurrentAccessToken().getToken();
        this.user_id = user_id;
        this.name = name;
        this.place_name = place_name;
        this.timestamp = timestap;
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
        this.attendersList = attendersList;
    }

    public Meeting() {
        this.fbtoken = AccessToken.getCurrentAccessToken().getToken();
    }

    public String getAttendersStringList() {
        String stringList = "";
        for (MeetingAttender attender: attendersList) {
            if(stringList.equals("")){
                stringList += attender.getUserId();
            }else {
                stringList += "," + attender.getUserId();
            }
        }
        return stringList;
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
        this.place_name = name;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
