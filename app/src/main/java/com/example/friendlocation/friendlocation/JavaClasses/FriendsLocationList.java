package com.example.friendlocation.friendlocation.JavaClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendsLocationList {
        @SerializedName("data")
        private List<FriendLocation> FriendLocations;

        public List<FriendLocation> getFriendLocations() {
                return FriendLocations;
        }

        public void setFriendLocations(List<FriendLocation> friendLocations) {
                FriendLocations = friendLocations;
        }
}
