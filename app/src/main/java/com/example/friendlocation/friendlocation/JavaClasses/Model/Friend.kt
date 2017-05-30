package com.example.friendlocation.friendlocation.JavaClasses.Model

import android.graphics.drawable.Drawable
import android.widget.ImageView

/**
 * Created by barte_000 on 19.03.2017.
 */

class Friend {
    var userId: String
    var name: String
    lateinit var avatar: Drawable

    constructor(userId: String, name: String, avatar: Drawable) {
        this.userId = userId
        this.name = name
        this.avatar = avatar
    }

    constructor(userId: String, name: String) {
        this.userId = userId
        this.name = name
    }


}
