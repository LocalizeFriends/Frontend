package com.example.friendlocation.friendlocation.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.Adapters.MeetingAdapter;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.R;

import java.util.List;

public class MeetingFragment extends ListFragment {

    ArrayAdapter<Meeting> adapter;
    private List<Meeting> meetingList;

    public MeetingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.fragment_meeting, container, false);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        meetingList = getMeetingsFromBackend();
        //Query.getFriendsLocation(AccessToken.getCurrentAccessToken().getToken(),getActivity());
        adapter = new MeetingAdapter(this.getActivity(), meetingList);
        setListAdapter(adapter);


        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Meeting f = meetingList.get(position);
                Toast.makeText(getContext(), ""+f.getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    List<Meeting> getMeetingsFromBackend(){
        return Query.getMeetingProposalsSync(getActivity());
    }

}
