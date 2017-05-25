package com.example.friendlocation.friendlocation.JavaClasses.Model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by barte_000 on 19.03.2017.
 */

public class Friend {
    String userId;
    String name;
    Drawable avatar;

    public Friend(String userId, String name, Drawable avatar) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
    }

    public Friend(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
