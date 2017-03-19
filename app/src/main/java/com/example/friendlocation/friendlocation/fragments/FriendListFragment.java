package com.example.friendlocation.friendlocation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.friendlocation.friendlocation.R;
import com.facebook.AccessToken;

public class FriendListFragment extends Fragment {

    AccessToken token;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        token = AccessToken.getCurrentAccessToken();
        token.getUserId();

        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }
}
