package com.example.friendlocation.friendlocation.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.Adapters.FriendsListAdapter;
import com.example.friendlocation.friendlocation.JavaClasses.Friend;
import com.example.friendlocation.friendlocation.R;
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

import static android.app.Activity.RESULT_OK;

public class FriendListFragment extends ListFragment {

    AccessToken token;
    private List<Friend> friendsList;
    ArrayAdapter<Friend> adapter;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inflate the layout for this fragment
        token = AccessToken.getCurrentAccessToken();
        getUserInformation(token);
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        populateFriendList();
        adapter = new FriendsListAdapter(this.getActivity(), friendsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend f = friendsList.get(position);
                Toast.makeText(getContext(), ""+f.getName(), Toast.LENGTH_SHORT).show();
                //imgs.recycle(); //recycle images

            }
        });
    }

    public void populateFriendList(){
        if(friendsList == null)
            friendsList = getFriends();
    }

    public void getUserInformation(AccessToken token){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
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
            friendsData = rs.getJSONArray("data");
            for (int i = 0; i < friendsData.length(); i++) {
                JSONObject friendJson = friendsData.getJSONObject(i);

                URL profilePicUrl = new URL(friendJson.getJSONObject("picture").getJSONObject("data").getString("url"));
                Bitmap profilePic= BitmapFactory.decodeStream(profilePicUrl.openConnection().getInputStream());

                friends.add(new Friend( friendJson.getString("id"),friendJson.getString("name"),new BitmapDrawable(profilePic)));
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
