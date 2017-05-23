package com.example.friendlocation.friendlocation.Adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.friendlocation.friendlocation.JavaClasses.Notification;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationAdapter {

    NotificationAdapter(RemoteMessage remoteMessage){

        switch (remoteMessage.getData().get("type")){
            case "meetup_proposal_invitation_received":
                //get organizer id by ("organizer_id")
                //get meetup id by ("meetup_id")
                Notification n = new Notification(remoteMessage.getData());
                break;
            case "meetup_proposal_invitation_change":
                //get user id by ("user_id")
                //get meetup id by ("meetup_id")
                //get new status by (new_status)
                break;
            case "meetup_proposal_cancel_change":
                //get meetup id by ("meetup_id")
                //get new status by (new_status)
        }
    }
/*
    void drawDialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder();
        builder.setMessage("Czy chcesz utworzyć spotkanie?");
        builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setMeetingName();
            }
        });
        builder.setNegativeButton("Odrzuć", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    */
}
