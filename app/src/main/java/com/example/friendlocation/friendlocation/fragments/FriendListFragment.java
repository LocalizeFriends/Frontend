package com.example.friendlocation.friendlocation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("userId", f.getUserId());
                getActivity().setResult(RESULT_OK, returnIntent);
                //imgs.recycle(); //recycle images
                getActivity().finish();
            }
        });
    }

    public void populateFriendList(){
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
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public List<Friend> getFriends(){
        List<Friend> friends = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JSONObject rs = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + token.getUserId() +"/friends",
                null,
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
                JSONObject friend = friendsData.getJSONObject(i);
                double friendId = friend.getDouble("id");
                String friendName = friend.getString("name");
                friends.add(new Friend( friendId,friendName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friends;
    }
}
