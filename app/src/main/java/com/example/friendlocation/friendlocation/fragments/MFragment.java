package com.example.friendlocation.friendlocation.fragments;

import com.example.friendlocation.friendlocation.PathDrawing.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.API;
import com.example.friendlocation.friendlocation.ApiCall;
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
import butterknife.ButterKnife;
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
    static Marker m;

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
                if(m != null){
                    m.remove(); //We allow only one marker.
                }
                //Add marker
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Marker");
                m = mGoogleMap.addMarker(marker);

                //Draw path
                String url = getMapsApiDirectionsUrl(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), m.getPosition());
                ReadTask downloadTask = new ReadTask(mGoogleMap);
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        });
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

    void zoomCamera(int zoom, int tilt){
        Toast.makeText(getActivity(), "MyLocation button clicked," + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
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
