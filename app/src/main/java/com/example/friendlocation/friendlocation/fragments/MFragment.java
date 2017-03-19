package com.example.friendlocation.friendlocation.fragments;

import com.example.friendlocation.friendlocation.PathDrawing.*;
import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.API;
import com.example.friendlocation.friendlocation.JavaClasses.ApiCall;
import com.example.friendlocation.friendlocation.R;
import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("MissingPermission")
    public class MFragment extends Fragment implements
                                            OnMapReadyCallback,
                                            LocationListener,
                                            GoogleApiClient.ConnectionCallbacks,
                                            GoogleApiClient.OnConnectionFailedListener
    {

    private ArrayList<MarkerOptions> markers;
    final private int MY_REQUEST_FINE_LOCATION = 124;
    final private int MY_REQUEST_COARSE_LOCATION = 125;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    SupportMapFragment supportMapFragment;
    API.APIInterface apiInterface;
    AccessToken token;
    static Marker meetingMarker;
    private final Calendar mcurrentTime = Calendar.getInstance();

    public MFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        apiInterface = API.getClient();
        token = AccessToken.getCurrentAccessToken();
        return inflater.inflate(R.layout.fragment_m, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        grantPermission();
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if(meetingMarker != null){
                    meetingMarker.remove(); //We allow only one marker.
                }
                //Add marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("");
                meetingMarker = mGoogleMap.addMarker(marker);

                drawDialogs(); // Draw ac -> name -> time;
                meetingMarker.showInfoWindow();

                drawPath();
            }
        });
    }
    void drawPath(){
        if(mLastLocation != null) {
            //Draw path
            String url = getMapsApiDirectionsUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), meetingMarker.getPosition());
            ReadTask downloadTask = new ReadTask(mGoogleMap);
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }

    void drawDialogs(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Nazwij spotkanie");
        builder.setView(inflater.inflate(R.layout.customdialog_m, null));

        builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog f = (Dialog) dialog;
                EditText inputTemp = (EditText) f.findViewById(R.id.MeetingName);
                meetingMarker.setTitle(inputTemp.getText().toString());
                setMeetingTime();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setMeetingTime(){
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                meetingMarker.setTitle(meetingMarker.getTitle() + " - " + String.format("%02d:%02d",selectedHour,selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void setAttendes(){

    }

    void grantPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } else {
            // Show rationale and request permission.
        }
    }

    void zoomCamera(int zoom, int tilt){
            Toast.makeText(getActivity(), "MyLocation button clicked," + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(zoom)                   // Sets the zoom
                    .tilt(tilt)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
}

    void sendApiCall(ApiCall myLocation){
            Call<ApiCall> query = apiInterface.sendApiCall(myLocation);
            query.enqueue(new Callback<ApiCall>() {
                @Override
                public void onResponse(Call<ApiCall> call, Response<ApiCall> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Correct sending to api", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ApiCall> call, Throwable t) {
                    Toast.makeText(getActivity(), "Incorrect sending to api", Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation!= null) {
            zoomCamera(17,30);
            ApiCall myLocation = new ApiCall(token.getToken(), mLastLocation.getLongitude(), mLastLocation.getLatitude());
            sendApiCall(myLocation);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_COARSE_LOCATION);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_REQUEST_FINE_LOCATION);
        }

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
