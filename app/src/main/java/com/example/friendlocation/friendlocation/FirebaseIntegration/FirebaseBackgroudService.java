package com.example.friendlocation.friendlocation.FirebaseIntegration;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.Intent;

import com.example.friendlocation.friendlocation.JavaClasses.Model.Notification;
import com.example.friendlocation.friendlocation.MainActivity;
import com.example.friendlocation.friendlocation.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseBackgroudService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String YES_ACTION = "android.intent.action.YES_ACTION";
    private static final String NO_ACTION = "android.intent.action.NO_ACTION";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Notification notification = new Notification(remoteMessage.getData());

            switch (notification.getType()){
                case "meetup_proposal_invitation_received":
                    sendNotification("Invitation", "Do you want accept meeting", notification.getMeetup_id());
                    break;
                case "meetup_proposal_invitation_change":
                    sendNotification("Invitation status changed.", notification.getMeetup_id() + " was " +
                            (notification.isNew_status()? "accepted": "declined"));
                    break;
                case "meetup_proposal_cancel_change":
                    sendNotification("Meetup status changed.", notification.getMeetup_id() + " was " +
                            (notification.isNew_status()? "accepted": "declined"));
            }
        }
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title,String messageBody, String meetupid) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //set intents and pending intents to call service on click of "dismiss" action button of notification
        Intent dismissIntent = new Intent();
        dismissIntent.setAction(NO_ACTION);
        dismissIntent.putExtra("meeting_id", meetupid);
        PendingIntent piCancel = PendingIntent.getBroadcast(this, 1,dismissIntent , PendingIntent.FLAG_ONE_SHOT);
                //PendingIntent.getService(this, 0, dismissIntent, 0);

        //set intents and pending intents to call service on click of "snooze" action button of notification
        Intent AcceptIntent  = new Intent();
        AcceptIntent .setAction(YES_ACTION);
        AcceptIntent.putExtra("meeting_id", meetupid);
        PendingIntent piAccept = PendingIntent.getBroadcast(this, 1, AcceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.com_facebook_button_send_background, "Accept", piAccept)
                .addAction(R.drawable.com_facebook_close, "Cancel", piCancel);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Integer.valueOf(meetupid)  /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title,String messageBody){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0  /* ID of notification */, notificationBuilder.build());
    }
}
