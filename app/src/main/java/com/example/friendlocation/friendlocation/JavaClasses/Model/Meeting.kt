package com.example.friendlocation.friendlocation.JavaClasses.Model

import com.facebook.AccessToken
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Meeting {

    @SerializedName("id")
    var id: Int = 0
    var fbtoken: String
    var user_id: String = ""
    internal var name: String = ""
    var place_name: String = ""
    @SerializedName("creation_timestamp_ms")
    var timestamp: Long = 0
    @SerializedName("latitude")
    var lat: Double = 0.toDouble()
    @SerializedName("longitude")
    var lng: Double = 0.toDouble()
    internal var attendersList: MutableList<MeetingAttender>? = null

    constructor(id:Int, user_id: String, name: String, place_name: String, timestap: Long, latLng: LatLng, attendersList: MutableList<MeetingAttender>) {
        this.id = id;
        this.fbtoken = AccessToken.getCurrentAccessToken().token
        this.user_id = user_id
        this.name = name
        this.place_name = place_name
        this.timestamp = timestap
        this.lat = latLng.latitude
        this.lng = latLng.longitude
        this.attendersList = attendersList
    }

    constructor() {
        this.fbtoken = AccessToken.getCurrentAccessToken().token
    }

    val attendersStringList: String
        get() {
            var stringList = ""
            for (attender in attendersList!!) {
                if (stringList == "") {
                    stringList += attender.getUserId()
                } else {
                    stringList += "," + attender.getUserId()
                }
            }
            return stringList
        }

    fun addAttendeeToList(attender: MeetingAttender) {
        if (attendersList == null)
            attendersList = ArrayList<MeetingAttender>()
        attendersList!!.add(attender)
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
        this.place_name = name
    }
}
