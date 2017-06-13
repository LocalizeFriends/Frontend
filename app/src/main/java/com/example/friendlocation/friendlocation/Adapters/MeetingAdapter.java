package com.example.friendlocation.friendlocation.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendlocation.friendlocation.API.Query;
import com.example.friendlocation.friendlocation.JavaClasses.Model.Meeting;
import com.example.friendlocation.friendlocation.R;

import java.util.List;

public class MeetingAdapter extends ArrayAdapter<Meeting>{

    private final List<Meeting> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
    }

    public MeetingAdapter(Activity context, List<Meeting> list) {
        super(context, R.layout.meeting_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.meeting_row, null);
            ViewHolder viewHolder = new com.example.friendlocation.friendlocation.Adapters.MeetingAdapter.ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.MeetingName);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        Button cancel = (Button) view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Query.sendMeetingCancel();
                Toast.makeText(getContext(), position + ": " + list.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        return view;
    }
}




