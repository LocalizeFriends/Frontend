package com.example.friendlocation.friendlocation.JavaClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MeetupProposalList {
    @SerializedName("data")
    private List<Meeting> meetings;

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}
