package com.example.friendlocation.friendlocation.JavaClasses.Model

class Notification {
    var type: String?
        internal set
    var meetup_id: String?
        internal set
    var organizer_id: String?
        internal set
    var user_id: String? = ""
        internal set
    var isNew_status: Boolean? = false
        internal set

    constructor(type: String, meetup_id: String, organizer_id: String) {
        this.type = type
        this.meetup_id = meetup_id
        this.organizer_id = organizer_id
    }


    constructor(map: Map<String, String>) {
        this.type = map["type"]
        this.meetup_id = map["meetup_id"]
        this.organizer_id = map["organizer_id"]
        this.user_id = map["user_id"]
        this.isNew_status = java.lang.Boolean.valueOf(map["new_status"])
        //Change to kotlin it will be better.
        //CheckNull // Not implemented
    }
}
