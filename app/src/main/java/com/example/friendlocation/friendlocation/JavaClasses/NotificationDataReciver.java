package com.example.friendlocation.friendlocation.JavaClasses;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.friendlocation.friendlocation.API.Query;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationDataReciver extends BroadcastReceiver {
    private static final String YES_ACTION = "android.intent.action.YES_ACTION";
    private static final String NO_ACTION = "android.intent.action.NO_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        int id = Integer.valueOf( intent.getStringExtra("meeting_id"));

        switch (action){
            case YES_ACTION :
                //send yes
                Query.sendMeetingAccept(id, 1);

                break;
            case NO_ACTION :
                //send no
                Query.sendMeetingAccept(id, 0);
                break;
        }
    }
}
