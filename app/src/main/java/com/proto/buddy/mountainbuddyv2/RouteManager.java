package com.proto.buddy.mountainbuddyv2;

import android.content.Context;

import com.proto.buddy.mountainbuddyv2.database.DatabaseHelper;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;

/**
 * Created by Phillip on 08.08.2015.
 */
public class RouteManager {

    private ArrayList<Route> allRoutes;

    private ArrayList<Route> myRoutes;

    private Route current;

    private DatabaseHelper db;

    public RouteManager(Context context) {
        db = new DatabaseHelper(context);

        allRoutes = db.getAllRoutes();


        // Just for testing at the moment
        myRoutes = new ArrayList<Route>();

        for (int i = 0; i < allRoutes.size()/2; i++){
            myRoutes.add(allRoutes.get(i));
        }

        current = null;

    }

    public Route getCurrent() {
        return current;
    }

    public void setCurrent(Route current) {
        this.current = current;
    }

    public ArrayList<Route> getAllRoutes() {
        return allRoutes;
    }

    public ArrayList<Route> getMyRoutes() {
        return myRoutes;
    }

    public void setAllRoutes(ArrayList<Route> allRoutes) {
        this.allRoutes = allRoutes;
    }

    public void setMyRoutes(ArrayList<Route> myRoutes) {
        this.myRoutes = myRoutes;
    }

    public void saveRoutes(){
        //to be filled
    }



}
