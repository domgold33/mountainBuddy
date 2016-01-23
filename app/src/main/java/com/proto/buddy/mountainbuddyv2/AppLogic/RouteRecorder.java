package com.proto.buddy.mountainbuddyv2.AppLogic;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.proto.buddy.mountainbuddyv2.model.Point;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dominik on 22.01.2016.
 * Processes relevant location data and saves it to a specified route. Also handles visual updates to the GoogleMap fragment in
 * the main screen of the application while tracking a route.
 */
public class RouteRecorder implements LocationListener {

    private final static int MILLISECONDS_BETWEEN_UPDATES = 2000;
    private final static int METERS_BETWEEN_UPDATES = 10;
    private static final String TAG = "RouteRecorder";

    private TextView distanceText;
    private TextView heightText;

    /*
    The route being recorded.
     */
    private Route route;
    private GoogleMap map;

    /**
     * Instance of LocationManager class, used to help with GPS data processing
     */
    private LocationManager locationManager;
    /**
     * The most recently processed position.
     */
    private Location currentBestLocation;
    /**
     * Distance that has been tracked.
     */
    private double distance;
    /**
     * A marker showing the current position on the map.
     */
    private Marker currentLocationMarker;
    /**
     * A line which shows the tracked route.
     */
    private Polyline routePolyline;
    private ArrayList<LatLng> polylineData;

    /**
     * Creates a new RouteRecorder responsible for processing data associated to the given route.
     * @param route The route that will be tracked.
     * @param map The GoogleMap object which is displayed in the application's main screen.
     * @param distanceText A UI element in the main screen, displays distance tracked
     * @param heightText A UI element in the main screen, displays current altitude
     * @param locationManager Android class, necessary for obtaining location data
     */
    public RouteRecorder(Route route, GoogleMap map, TextView distanceText, TextView heightText, LocationManager locationManager){
        this.map = map;
        this.route = route;
        this.distanceText = distanceText;
        this.heightText = heightText;
        this.locationManager = locationManager;
        polylineData = new ArrayList<>();
    }

    @Override
    public void onLocationChanged(Location location) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(location.getTime());
        String time = format.format(date);
        Log.d(TAG, "****** onLocationChanged start");
        if(isBetterLocation(location)) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            Point point = new Point(latitude, longitude, altitude, time, -1);
            if(currentBestLocation == null){
                distance = 0;
                this.distanceText.setText(String.valueOf(distance));
                this.heightText.setText(String.valueOf(altitude));
                this.route.setStartPoint(point);
            }else {
                this.route.addOtherPoint(point);
                distance += (double) location.distanceTo(currentBestLocation);
                this.distanceText.setText(String.valueOf(Math.round(distance)) + "m");
                this.heightText.setText(String.valueOf(Math.round(altitude)));
            }

            Log.d(TAG, "Latitude: " + latitude);
            Log.d(TAG, "Longitude: " + longitude);
            Log.d(TAG, "Altitude: " + altitude); // immer 0

            currentBestLocation = location;
            updateMap(latitude, longitude);
        }
        Log.d(TAG, "Time: " + time);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Updates the map visually by the given location data
     * @param latitude Latitude of current location
     * @param longitude Longitude of current location
     */
    private void updateMap(double latitude, double longitude){
        if(map != null) {
            LatLng newLatLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 10));
            if(currentLocationMarker == null) {
                currentLocationMarker = map.addMarker(new MarkerOptions()
                        .position(newLatLng)
                        .title("You"));
            }else{
                currentLocationMarker.setPosition(newLatLng);
            }
            if(routePolyline == null){
                polylineData.add(newLatLng);
                routePolyline = map.addPolyline(new PolylineOptions()
                        .color(Color.BLUE));
            }else{
                polylineData.add(newLatLng);
                routePolyline.setPoints(polylineData);
            }
        }
    }

    /**
     * Checks whether a newly obtained location is better in accordance to recency and accuracy than the most recent one.
     * @param newLocation The new location, to be checked
     * @return Whether the given location is better
     */
    private boolean isBetterLocation(Location newLocation){
        final int ONE_MINUTE = 1000 * 60;
        if(currentBestLocation == null){
            return true;
        }

        long deltaTime = newLocation.getTime() - currentBestLocation.getTime();
        boolean newerThanOneMin = deltaTime > ONE_MINUTE;
        boolean olderThanOneMin = deltaTime < -ONE_MINUTE;
        boolean newer = deltaTime > 0;

        if(newerThanOneMin){
            return true;
        }else if(olderThanOneMin){
            return false;
        }

        float deltaAccuracy = newLocation.getAccuracy() - currentBestLocation.getAccuracy();
        boolean isLessAccurate = deltaAccuracy > 0;
        boolean isMoreAccurate = deltaAccuracy < 0;
        if(isMoreAccurate){
            return true;
        }else if(newer && !isLessAccurate){
            return true;
        }
        return false;
    }

    public void requestLocationUpdates(String provider){
        locationManager.requestLocationUpdates(provider, MILLISECONDS_BETWEEN_UPDATES, METERS_BETWEEN_UPDATES, this);
    }

    public void cancelLocationUpdates(){
        locationManager.removeUpdates(this);
    }

}
