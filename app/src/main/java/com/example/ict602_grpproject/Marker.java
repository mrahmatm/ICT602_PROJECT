package com.example.ict602_grpproject;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;

public class Marker {
    //object class for marker, tapi lekluh
    private Location location;
    private String hazardName;
    private String hazardType;
    private String hazardDesc;
    private int userID;
    private Time timeReported;

    public Marker(Location inputLocation, String inputName, String inputType, String inputDesc, int inputID, Time inputTime){
        location = inputLocation;
        hazardName = inputName;
        hazardType = inputType;
        hazardDesc = inputDesc;
        userID = inputID;
        timeReported = inputTime;
    }

    public Location getLocation() {
        return location;
    }

    public String getHazardDesc() {
        return hazardDesc;
    }

    public String getHazardName() {
        return hazardName;
    }

    public String getHazardType() {
        return hazardType;
    }

    public Time getTimeReported(){
        return timeReported;
    }

    public int getUserID() {
        return userID;
    }
}
