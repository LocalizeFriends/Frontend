package com.example.friendlocation.friendlocation.fragments;

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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.API;
import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.Adapters.FriendsListAdapter;
import com.example.friendlocation.friendlocation.JavaClasses.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.Friend;
import com.example.friendlocation.friendlocation.JavaClasses.FriendLocation;
import com.example.friendlocation.friendlocation.JavaClasses.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.MeetingAttender;
import com.example.friendlocation.friendlocation.JavaClasses.MyFirebaseInstanceIDService;
import com.example.friendlocation.friendlocation.PathDrawing.ReadTask;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    static API.APIInterface apiInterface;
    AccessToken token;
    static Marker meetingMarker;
    private List<Marker> friendMarkerList = new ArrayList<>();
    private final Calendar mCurrentTime = Calendar.getInstance();
    private List<Friend> friendList;
    static Meeting meeting;
    @BindView(R.id.find_friends) Button find_friends;
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
        View view = inflater.inflate(R.layout.fragment_m, container, false);
        ButterKnife.bind(this, view);
        mGoogleApiClient.connect();
        apiInterface = API.getClient();
        token = AccessToken.getCurrentAccessToken();
        MyFirebaseInstanceIDService s = new MyFirebaseInstanceIDService();
        s.onTokenRefresh();

        find_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendMarkerList.clear();
                List<FriendLocation> nerbyFriendLocations = Query.getFriendsLocationWithinRange(AccessToken.getCurrentAccessToken().getToken(),
                        mLastLocation.getLongitude(), mLastLocation.getLatitude(), 1000,
                        getActivity());

                for (FriendLocation f : nerbyFriendLocations){
                    MarkerOptions friendMarker = new MarkerOptions().position( new LatLng(f.getLocation().getLng(),
                            f.getLocation().getLat())).title(f.getName()).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    friendMarkerList.add(mGoogleMap.addMarker(friendMarker));
                }

                
            }
        });

        return view;
    }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
            if (mapFragment == null) {
                mapFragment = new SupportMapFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.map_container, mapFragment, "mapFragment");
                ft.commit();
                fm.executePendingTransactions();
            }
            mapFragment.getMapAsync(this);
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //getMeetings()
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
                meeting = new Meeting();
                drawDialogs(); // Draw ac -> name -> time -> attendees;
                meetingMarker.showInfoWindow();
                drawPath();

                //send meeting

            }
        });
    }
    void drawPath(){
        if(mLastLocation != null) {
            //Draw path
            String url = getMapsApiDirectionsUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), meetingMarker.getPosition());
            //meeting.setLat(meetingMarker.getPosition().latitude);
            //meeting.setLat(meetingMarker.getPosition().longitude);



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
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                meetingMarker.setTitle(meetingMarker.getTitle() + " - " + String.format("%02d:%02d",selectedHour,selectedMinute));

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
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final ArrayAdapter<Friend> adapter = getPossibleAttendees();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Friend friend = adapter.getItem(which);
                Toast.makeText(getContext(), ""+friend.getName(), Toast.LENGTH_SHORT).show();
                meeting.addAttendeeToList(new MeetingAttender(friend.getUserId(), true));
                Query.sendMeetupProposal(meeting, getActivity());
            }
        });
        builder.show();
    }

    ArrayAdapter<Friend> getPossibleAttendees(){
        if(friendList == null)
             friendList = Query.getFriends();

        ArrayAdapter<Friend> adapter = new FriendsListAdapter(this.getActivity(), friendList);
        return adapter;
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
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to Location user
                    .zoom(zoom)                   // Sets the zoom
                    .tilt(tilt)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
        grantPermission();

        if(mLastLocation!= null) {
            zoomCamera(17,30);
            ApiCall myLocation = new ApiCall(token.getToken(), mLastLocation.getLongitude(), mLastLocation.getLatitude());
            Query.sendApiCall(myLocation, getActivity());
        }
        /*
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        */
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
