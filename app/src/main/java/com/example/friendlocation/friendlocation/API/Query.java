package com.example.friendlocation.friendlocation.API;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.JavaClasses.ApiCall;
import com.example.friendlocation.friendlocation.JavaClasses.Friend;
import com.example.friendlocation.friendlocation.JavaClasses.Meeting;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by barte_000 on 20.03.2017.
 */

public class Query {

    static API.APIInterface apiInterface = API.getClient();
    static List<Meeting> meetings = new ArrayList<Meeting>();

    public static void sendApiCall(ApiCall myLocation, final Activity activity){
        Call<ApiCall> query = apiInterface.sendApiCall(myLocation.getFbtoken(), myLocation.getLat(), myLocation.getLng());
        query.enqueue(new Callback<ApiCall>() {
            @Override
            public void onResponse(Call<ApiCall> call, Response<ApiCall> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("sendApiCall onResponse", response.errorBody().string());
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

    public static void sendMeeting(Meeting meeting, final Activity activity){
        Call<Meeting> query = apiInterface.sendMeeting(meeting);
        query.enqueue(new Callback<Meeting>() {

            @Override
            public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("sendMeeting onResponse", response.errorBody().string());
                } catch (IOException e) {}

                Toast.makeText(activity, "Correct sending meeting to api", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Meeting> call, Throwable t) {
                Toast.makeText(activity, "Incorrect sending meeting to api", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<Meeting> getMeetings(final Activity activity){

        Call<List<Meeting>> query = apiInterface.getMeetings();
        query.enqueue(new Callback<List<Meeting>>() {

            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("getMeetings onResponse", response.errorBody().string());
                } catch (IOException e) {}

                Toast.makeText(activity, "Correct get meetings", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    meetings.clear();
                    meetings.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
        return meetings;
    }

    public static List<Meeting> getFriendsLocation(final Activity activity){

        Call<List<Meeting>> query = apiInterface.getMeetings();
        query.enqueue(new Callback<List<Meeting>>() {

            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                try {
                    if(response.errorBody() !=null)
                        Log.d("getMeetings onResponse", response.errorBody().string());
                } catch (IOException e) {}

                Toast.makeText(activity, "Correct get meetings", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()){
                    meetings.clear();
                    meetings.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
        return meetings;
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
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    static public List<Friend> getFriends(){
        List<Friend> friends = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, picture.type(large)");

        JSONObject rs = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() +"/friends",
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
