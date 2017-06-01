package com.example.friendlocation.friendlocation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.API;
import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.JavaClasses.Model.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Friend;
import com.example.friendlocation.friendlocation.JavaClasses.Model.FriendLocation;
import com.example.friendlocation.friendlocation.JavaClasses.MarkersVizualizer;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.MeetupSeting;
import com.example.friendlocation.friendlocation.FirebaseIntegration.MyFirebaseInstanceIDService;
import com.example.friendlocation.friendlocation.R;
import com.facebook.AccessToken;
import com.google.ads.mediation.customevent.CustomEventAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferObserver;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("MissingPermission")
public class MFragment extends Fragment implements
                                            OnMapReadyCallback,
                                            LocationListener,
                                            GoogleApiClient.ConnectionCallbacks,
                                            GoogleApiClient.OnConnectionFailedListener,
                                            Observer

{

    final private int MY_REQUEST_FINE_LOCATION = 124;
    final private int MY_REQUEST_COARSE_LOCATION = 125;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    static API.APIInterface apiInterface;
    static Marker meetingMarker;
    private List<Marker> friendMarkerList = new ArrayList<>();
    private Observable<List<Meeting>> friendss = new Observable<List<Meeting>>() {

    };
    private List<Friend> friendList;
    List<Meeting> meetupProposalList;
    List<FriendLocation> nerbyFriendLocations;

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
        MyFirebaseInstanceIDService s = new MyFirebaseInstanceIDService();
        s.onTokenRefresh();



        find_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nerbyFriendLocations = Query.getFriendsLocationWithinRangeSync(
                        mLastLocation.getLongitude(), mLastLocation.getLatitude(), 10000,
                        getActivity());
                meetupProposalList = checkForMeetings();

                if(nerbyFriendLocations != null)
                    drawFrienedsInRange(nerbyFriendLocations);
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

        //getMeetings
        meetupProposalList = checkForMeetings();
        if(meetupProposalList != null)
            friendss.registerObserver(meetupProposalList);
            new MarkersVizualizer(mGoogleMap, meetupProposalList);

        //setMeetings()
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
            new MeetupSeting(getActivity(),mGoogleMap,meetingMarker,mLastLocation,point,friendList);
            }
        });


    }

    List<Meeting> checkForMeetings(){
        return  Query.getMeetingProposalsSync(getActivity());
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
            zoomCamera(4,30);

            DecimalFormat df = new DecimalFormat("#.######", new DecimalFormatSymbols(Locale.US));

            ApiCall myLocation = new ApiCall(AccessToken.getCurrentAccessToken().getToken(), Double.valueOf(df.format(mLastLocation.getLongitude())),Double.valueOf(df.format(mLastLocation.getLatitude())));
            Query.sendApiCall(myLocation, getActivity());
        }
        /*
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        */
    }

    void drawFrienedsInRange(List<FriendLocation> nerbyFriendLocations){
        if(nerbyFriendLocations != null){
            for (FriendLocation f : nerbyFriendLocations){
                MarkerOptions friendMarker = new MarkerOptions().position( new LatLng(f.getLocation().getLat(),
                        f.getLocation().getLng())).title(f.getName())
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                friendMarkerList.add(mGoogleMap.addMarker(friendMarker));
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void update(java.util.Observable o, Object arg) {
        Toast.makeText(getActivity(), "Failure on accept meeting- connection problem", Toast.LENGTH_SHORT).show();
    }
}
