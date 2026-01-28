package com.example.locationtracker;

public class LocationModel { //Location class
    private final int id;
    private final double latitude;
    private final double longitude;
    private final long timestamp;

    public LocationModel(int id, double latitude, double longitude, long timestamp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public long getTimestamp() { return timestamp; }
}