package com.example.friendlocation.friendlocation.JavaClasses;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.location.Location;

import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.Adapters.FriendsListAdapter;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Friend;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.Model.MeetingAttender;
import com.example.friendlocation.friendlocation.PathDrawing.ReadTask;
import com.example.friendlocation.friendlocation.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MeetupSeting {
    Activity currentActivity;
    Location mLastLocation;
    GoogleMap mGoogleMap;
    private final Calendar mCurrentTime = Calendar.getInstance();
    MarkerOptions meetingMarker;
    static Meeting meeting;
    private List<Friend> friendList;

    public MeetupSeting(Activity activity, GoogleMap googleMap, Marker lastMarker, Location lastLocation, LatLng point, List<Friend> friendList){
        currentActivity = activity;
        mGoogleMap = googleMap;
        mLastLocation = lastLocation;
        this.friendList = friendList;

        meetingMarker = setMeetingMarker(point);
        meeting = new Meeting();
        drawDialogs(); // Draw ac -> name -> time -> attendees;

        //meetingMarker.showInfoWindow();

        //TODO find a way to remove bad markers.
    }

    void drawPath(Location mLastLocation, Marker meetingMarker){
        if(mLastLocation != null) {
            //Draw path
            String url = getMapsApiDirectionsUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), meetingMarker.getPosition());

            DecimalFormat df = new DecimalFormat("#.######", new DecimalFormatSymbols(Locale.US));
            meeting.setLng( Double.valueOf(df.format( meetingMarker.getPosition().longitude)));
            meeting.setLat( Double.valueOf(df.format( meetingMarker.getPosition().latitude)));

            ReadTask downloadTask = new ReadTask(mGoogleMap);
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    void drawDialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
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

    void setMeetingName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        final LayoutInflater inflater = currentActivity.getLayoutInflater();
        builder.setMessage("Nazwij spotkanie");
        builder.setView(inflater.inflate(R.layout.customdialog_m, null));

        builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog f = (Dialog) dialog;
                EditText inputTemp = (EditText) f.findViewById(R.id.MeetingName);
                meetingMarker.title(inputTemp.getText().toString());
                meeting.setName(inputTemp.getText().toString());
                setMeetingTime();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setMeetingTime(){
        final int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(currentActivity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                meetingMarker.title(meetingMarker.getTitle() + " - " + String.format("%02d:%02d",selectedHour,selectedMinute));

                Calendar d = Calendar.getInstance();
                d.setTimeZone(TimeZone.getTimeZone("UTC"));
                d.set(Calendar.HOUR_OF_DAY, selectedHour);
                d.set(Calendar.MINUTE, selectedMinute);
                meeting.setTimestamp(d.getTimeInMillis());
                //TODO sprawdzic czy to dziala
                setAttendees();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void setAttendees(){
        final LayoutInflater inflater = currentActivity.getLayoutInflater();
        final ArrayAdapter<Friend> adapter = getPossibleAttendees();

        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Friend friend = adapter.getItem(which);
                Toast.makeText(currentActivity, ""+friend.getName(), Toast.LENGTH_SHORT).show();
                meeting.addAttendeeToList(new MeetingAttender(friend.getUserId(), true));
                Query.sendMeetupProposal(meeting, currentActivity);
                Marker m = mGoogleMap.addMarker(new MarkerOptions().position(meetingMarker.getPosition()
                ).title(meetingMarker.getTitle()));
                drawPath(mLastLocation, m);
                //Ta linijka jest glupia -.- -> workaround
            }
        });
        builder.show();
    }

    MarkerOptions setMeetingMarker(LatLng point){
        //Add marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(point.latitude, point.longitude)).title("");
        return marker ;//= mGoogleMap.addMarker(marker);
    }

    ArrayAdapter<Friend> getPossibleAttendees(){
        if(friendList == null)
            friendList = Query.getFriends();

        ArrayAdapter<Friend> adapter = new FriendsListAdapter(currentActivity, friendList);
        return adapter;
    }

    private String getMapsApiDirectionsUrl(LatLng origin,LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "transit";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }


}
