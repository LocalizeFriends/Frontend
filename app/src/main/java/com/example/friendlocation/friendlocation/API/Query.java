package com.example.friendlocation.friendlocation.API;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.JavaClasses.Model.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Friend;
import com.example.friendlocation.friendlocation.JavaClasses.Model.FriendLocation;
import com.example.friendlocation.friendlocation.JavaClasses.ListModels.FriendsLocationList;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.JavaClasses.ListModels.MeetupProposalList;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by barte_000 on 20.03.2017.
 */

public class Query {

    static API.APIInterface apiInterface = API.getClient();
    static List<Meeting> meetingProposals = new ArrayList<Meeting>();
    static List<FriendLocation> FriendLocations = new ArrayList<>();
    static FriendsLocationList FriendLocationsWithinRange;


    public static void  sendApiCall(ApiCall myLocation, final Activity activity){
        Call<ApiCall> query = apiInterface.sendApiCall(myLocation.getFbtoken(), myLocation.getLat(), myLocation.getLng());
        query.enqueue(new Callback<ApiCall>() {
            @Override
            public void onResponse(Call<ApiCall> call, Response<ApiCall> response) {
                try {
                    if(response.errorBody() !=null){
                        //Log.d("sendApiCall onResponse", response.errorBody().string());
                        String s = response.errorBody().string();
                        Log.d("dupa", s);
                    }
                } catch (IOException e) {}

                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Correct sending to api", Toast.LENGTH_SHORT).show();
                    Log.d("sendApiCall onSResponse", response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<ApiCall> call, Throwable t) {
                Toast.makeText(activity, "Incorrect sending to api", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMeetupProposal(Meeting meeting, final Activity activity){
        Call<Meeting> query = apiInterface.sendMeetupProposal(meeting.getFbtoken(),
                meeting.getName(), meeting.getTimestamp(), meeting.getPlace_name(),
                meeting.getLng(), meeting.getLat(), meeting.getAttendersStringList());

        query.enqueue(new Callback<Meeting>() {

            @Override
            public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("sendMeeting onResponse", response.errorBody().string());
                } catch (IOException e) {}

                if(response.isSuccessful()){
                    Toast.makeText(activity, "Correct sending meeting to api", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "Incorrect data code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Meeting> call, Throwable t) {
                Toast.makeText(activity, "Incorrect sending meeting to api", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMessagingAddress(String firebaseToken){
        Call<ResponseBody> query = apiInterface.sendMessagingAddress(AccessToken.getCurrentAccessToken().getToken(),firebaseToken,"1541934671");

        query.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if(response.errorBody() !=null)
                        Log.d("sendNotifi onResponse", response.errorBody().string());
                } catch (IOException e) {}

                if(response.isSuccessful()){
                    Log.d("FirebaseID", "Success");
                }else{
                    Log.d("FirebaseID", "Undefined Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("FirebaseID", "Error");
                //Toast.makeText(activity, "Incorrect sending notifi to api", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMeetingCancel(int meetingId, boolean b, final Activity activity){
        Call<ResponseBody> query = apiInterface.sendMeetingCancel(AccessToken.getCurrentAccessToken().getToken(), b, meetingId);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(activity, "Correct cancel meeting", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "Something goes wrong with send cancel", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Failure on cancel meeting", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMeetingAccept(int meetingId, int b, final Activity activity){
        Call<ResponseBody> query = apiInterface.sendMeetingAccept(AccessToken.getCurrentAccessToken().getToken(), b, meetingId);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(activity, "Correct accept meeting", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(activity, "Something goes wrong with send accept - meeting exist?", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity, "Failure on accept meeting- connection problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void sendMeetingAccept(int meetingId, int b){
        Call<ResponseBody> query = apiInterface.sendMeetingAccept(AccessToken.getCurrentAccessToken().getToken(), b, meetingId);
        query.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                   // Toast.makeText(activity, "Correct accept meeting", Toast.LENGTH_SHORT).show();
                    Log.d("Meeting send aceptation", response.body().toString());
                }else{
                   // Toast.makeText(activity, "Something goes wrong with send accept - meeting exist?", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(activity, "Failure on accept meeting- connection problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<Meeting> getMeetingProposalsSync(final Activity activity){
        Call<MeetupProposalList> query = apiInterface.getMeetings(AccessToken.getCurrentAccessToken().getToken());

        MeetupProposalList meetingList = null;
        try {
            meetingList = query.execute().body(); //TODO error handling.
        } catch (IOException e) {
            System.err.println("An exception was thrown");

        }
        return meetingList == null ? null : meetingList.getMeetings();
    }

    public static List<FriendLocation> getFriendsLocation(String fbtoken , final Activity activity){

        Call <FriendsLocationList> query = apiInterface.getFriendsLocation(fbtoken);
        query.enqueue(new Callback<FriendsLocationList>() {

            @Override
            public void onResponse(Call<FriendsLocationList> call, Response<FriendsLocationList> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("getMeetings onResponse", response.errorBody().string());
                } catch (IOException e) {}

                Toast.makeText(activity, "Correct get meetings", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    FriendLocations = response.body().getFriendLocations();
                }
            }

            @Override
            public void onFailure(Call<FriendsLocationList> call, Throwable t) {
                Toast.makeText(activity, "Failed to connect backend!", Toast.LENGTH_SHORT).show();
            }
        });
        return FriendLocations;
    }

    public static List<FriendLocation> getFriendsLocationWithinRange(double lng, double lat, int meters, final Activity activity){

        DecimalFormat df = new DecimalFormat("#.#######", new DecimalFormatSymbols(Locale.US));
        Call <FriendsLocationList> query = apiInterface.getFriendsWithinRange(
                AccessToken.getCurrentAccessToken().getToken(),
                Double.valueOf(df.format( lng)),
                Double.valueOf(df.format( lat)),
                meters);

        query.enqueue(new Callback<FriendsLocationList>() {

            @Override
            public void onResponse(Call<FriendsLocationList> call, Response<FriendsLocationList> response) {
                Toast.makeText(activity, "Correct get friend in radius", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    FriendLocationsWithinRange = response.body();
                }
            }

            @Override
            public void onFailure(Call<FriendsLocationList> call, Throwable t) {
                Toast.makeText(activity, "Failed to connect backend!", Toast.LENGTH_SHORT).show();
            }
        });
        return FriendLocationsWithinRange == null? null : FriendLocationsWithinRange.getFriendLocations();
    }

    public static List<FriendLocation> getFriendsLocationWithinRangeSync(double lng, double lat, int meters, final Activity activity){

        DecimalFormat df = new DecimalFormat("#.#######", new DecimalFormatSymbols(Locale.US));
        Call <FriendsLocationList> query = apiInterface.getFriendsWithinRange(
                AccessToken.getCurrentAccessToken().getToken(),
                Double.valueOf(df.format( lng)),
                Double.valueOf(df.format( lat)),
                meters);

        try {
            FriendLocationsWithinRange = query.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FriendLocationsWithinRange == null? null : FriendLocationsWithinRange.getFriendLocations();
    }


    //##################### FacebookApi #####################
    static public void getUserInformation(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("LoginActivity", response.toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, Location, picture.type(large), friends");
        request.setParameters(parameters);
        request.executeAsync();
    }

    static public List<Friend> getFriends(){
        List<Friend> friends = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle parameters = new Bundle();
        parameters.putString("fields",/*"context.fields(friends_using_app)" );//*/"id, name, email, user_friends, friends, picture.type(large)");

        JSONObject rs = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                /*"/1908151672751269", //*/"/" + AccessToken.getCurrentAccessToken().getUserId() +"/friends",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        //rs.put( response.getJSONObject());
                    }
                }
        ).executeAndWait().getJSONObject();

        JSONArray friendsData = null;
        try {
            if(rs != null){
            friendsData = rs.getJSONArray("data");

                for (int i = 0; i < friendsData.length(); i++) {
                    JSONObject friendJson = friendsData.getJSONObject(i);

                    URL profilePicUrl = new URL(friendJson.getJSONObject("picture").getJSONObject("data").getString("url"));
                    Bitmap profilePic = BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream());

                    friends.add(new Friend(friendJson.getString("id"), friendJson.getString("name"), new BitmapDrawable(profilePic)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return friends;
    }
}
