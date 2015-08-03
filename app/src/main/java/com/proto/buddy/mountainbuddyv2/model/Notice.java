package com.proto.buddy.mountainbuddyv2.model;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public abstract class Notice implements Serializable{

    /**
     * ID der Notiz in der Datenbank
     */
    private int id;

    /**
     * Ort an dem die Notiz erstellt wurde
     */
    private Point place;

    /**
     * Titel der Notiz
     */
    private String title;

    /**
     * Beschreibung der Notiz
     */
    private String text;
    private long routeId;

    public Notice(Point place, String title, String text) {
        this.place = place;
        this.title = title;
        this.text = text;
    }

    public Notice(Point place){
        this.place = place;
        this.title = "";
        this.text = "";
    }

    public Notice(){

    }

    /**
     * @return
     */
    public Point getPlace() {

        return this.place;
    }

    /**
     * @param point
     */
    public void setPlace(Point point) {

        this.place = point;
    }

    /**
     * @return
     */
    public String getTitle() {

        return this.title;
    }

    /**
     * @param newTitle
     */
    public void setTitle(String newTitle) {

        this.title = newTitle;
    }

    /**
     * @return
     */
    public String getText() {

        return this.text;
    }

    /**
     * @param newText
     */
    public void setText(String newText) {

        this.text = newText;
    }

    public long getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }
}