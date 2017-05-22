package com.example.friendlocation.friendlocation.JavaClasses;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MarkersVizualizer {
    private ArrayList<Marker> markers = new ArrayList<>();

    public MarkersVizualizer(GoogleMap googleMap, List<Meeting> meetings){

        for (Meeting meet: meetings) {
            markers.add(setMeetingMarker(meet, googleMap)) ;
        }

    }

    Marker setMeetingMarker(Meeting m, GoogleMap mGoogleMap){

        //Add marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(m.lat, m.lng)).title(m.getName());
        return mGoogleMap.addMarker(marker);
    }

}
