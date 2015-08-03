package com.proto.buddy.mountainbuddyv2.model;

import java.io.Serializable;
import java.util.Date;

public class Point implements Serializable{

    private long id;
    private double latitude;
    private double longitude;
    private double altitude;
    private String description;
    private String time;
    private long routeId;

    public Point(double latitude, double longitude, double altitude, String time, long routeId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.time = time;
        this.description = "";
        this.id = -1;
        this.routeId = routeId;
    }

    public Point() {
        this.latitude = 0;
        this.longitude = 0;
        this.altitude = 0;
        this.time = "";
        this.description = "";
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getRouteId() {
        return routeId;
    }

    @Override
    public String toString() {
        return "Point{" +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", time='" + time + '\'' +
                '}';
    }
}
