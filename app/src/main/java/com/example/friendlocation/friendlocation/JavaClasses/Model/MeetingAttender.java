package com.example.friendlocation.friendlocation.JavaClasses.Model;

public class MeetingAttender {
    String userId;
    Boolean meetingAcceptation;

    public MeetingAttender(String userId, Boolean meetingAcceptation) {
        this.userId = userId;
        this.meetingAcceptation = meetingAcceptation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getMeetingAcceptation() {
        return meetingAcceptation;
    }

    public void setMeetingAcceptation(Boolean meetingAcceptation) {
        this.meetingAcceptation = meetingAcceptation;
    }
}
