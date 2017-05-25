package com.example.friendlocation.friendlocation.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.Adapters.FriendsListAdapter;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Friend;
import com.example.friendlocation.friendlocation.R;
import com.facebook.AccessToken;

import java.util.List;
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
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        populateFriendList();
        Query.getFriendsLocation(AccessToken.getCurrentAccessToken().getToken(),getActivity());
        adapter = new FriendsListAdapter(this.getActivity(), friendsList);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend f = friendsList.get(position);
                Toast.makeText(getContext(), ""+f.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void populateFriendList(){
        if(friendsList == null)
            friendsList = Query.getFriends();
    }
}
