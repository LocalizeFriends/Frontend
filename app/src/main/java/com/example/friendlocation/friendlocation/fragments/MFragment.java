package com.example.friendlocation.friendlocation.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendlocation.friendlocation.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MFragment extends Fragment {

    private ArrayList<MarkerOptions> markers;
    private GoogleMap mGoogleMap;
    private static SupportMapFragment mapFragment;
    final private int MY_REQUEST_FINE_LOCATION = 124;
    final private int MY_REQUEST_COARSE_LOCATION = 125;


    public MFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mapFragment = new SupportMapFragment();
        // Create an instance of GoogleAPIClient.1

        return inflater.inflate(R.layout.fragment_m, container, false);
    }

    }
