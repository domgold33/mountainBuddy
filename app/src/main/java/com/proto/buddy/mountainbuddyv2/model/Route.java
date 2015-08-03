package com.proto.buddy.mountainbuddyv2.model;

import java.io.Serializable;
import java.util.*;

/**
 * 
 */
public class Route implements Serializable {

    /**
     * ID der Route in der Datenbank.
     */
    private int id;

    /**
     * Startpunkt der Route.
     */
    private Point startPoint;

    /**
     * Endpunkt der Route.
     */
    private Point endPoint;

    /**
     * Liste an aufgezeichneten Zwischenpunkten.
     */
    private ArrayList<Point> otherPoints;

    /**
     * Name der Route
     */
    private String name;

    /**
     * Liste an Notizen/Fotos
     */
    private ArrayList<Notice> listOfNotices;

    /**
     * 
     */
    //private Statistics statistics;

    /**
     * Beschreibung der Route.
     */
    private String description;

    /**
     * Erzeugt eine leere Route.
     */
    public Route() {
        this.otherPoints = new ArrayList<>();
        this.listOfNotices = new ArrayList<>();
    }

    /**
     * Erzeugt eine Route mit Start-, Endpunkt und Name.
     * @param startPoint Startpunkt
     * @param endPoint Endpunkt
     * @param name Name der Route
     */
    public Route(Point startPoint, Point endPoint, String name) {
        this.id = -1;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.name  = name;
        this.otherPoints = new ArrayList<>();
        this.listOfNotices = new ArrayList<>();
        this.description = "";
    }

    public Route(String name) {
        this.id = -1;
        this.name  = name;
        this.otherPoints = new ArrayList<>();
        this.listOfNotices = new ArrayList<>();
        this.startPoint = null;
        this.endPoint = null;
        this.description = "";
    }

    /**
     * @return
     */
    public Point getStartPoint() {
        return this.startPoint;
    }

    /**
     * @param startPoint
     */
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * @return
     */
    public Point getEndPoint() {
        return this.endPoint;
    }

    /**
     * @param newEndPoint
     */
    public void setEndPoint(Point newEndPoint) {
        this.endPoint = newEndPoint;
    }

    /**
     * @return
     */
    public ArrayList<Point> getOtherPoints() {
        return this.otherPoints;
    }

    public void addOtherPoint(Point point){
        this.otherPoints.add(point);
    }

    public ArrayList<Notice> getListOfNotices(){
        return this.listOfNotices;
    }

    public void addNotice(Notice notice){
        this.listOfNotices.add(notice);
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param newName
     */
    public void setName(String newName) {
        this.name = newName;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description){
        return this.description = description;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", otherPoints=" + otherPoints +
                ", name='" + name + '\'' +
                ", listOfNotices=" + listOfNotices +
                ", description='" + description + '\'' +
                '}';
    }
}