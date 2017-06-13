package com.example.friendlocation.friendlocation.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friendlocation.friendlocation.JavaClasses.Model.Friend;
import com.example.friendlocation.friendlocation.R;

import java.util.List;

public class FriendsListAdapter extends ArrayAdapter<Friend> {

    private final List<Friend> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView avatar;
    }

    public FriendsListAdapter(Activity context, List<Friend> list) {
        super(context, R.layout.friend_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.friend_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.friendName);
            viewHolder.avatar = (ImageView) view.findViewById(R.id.friendImg);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.avatar.setImageDrawable(list.get(position).getAvatar());
        return view;
    }
}

