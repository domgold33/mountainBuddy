package com.proto.buddy.mountainbuddyv2.database;

/**
 * Created by Alexander Buyanov
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.proto.buddy.mountainbuddyv2.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Log
    private static final String LOG = "DatabaseHelper";
    private static final String DATABASE_NAME = "mountainbuddy_db";
    private static final String FILE_DIR = "MountainBuddy";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ROUTES = "routes";
    private static final String TABLE_POINTS = "points";
    private static final String TABLE_ROUTE_HAS_POINTS = "routeHasPoints";
    private static final String TABLE_NOTICE = "notice";

    // Common column names
    private static final String KEY_ID = "id";

    // uses table - column names
    private static final String USERS_NAME = "name";
    private static final String USERS_PASSWORD = "password";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_STAISTIK_ID = "staistikId";
    private static final String USERS_ROUTE_FAV_ID = "routeFavId";
    private static final String USERS_ROUTE_SELF_ID = "routeFavId";

    // routes table - column names
    private static final String ROUTES_ROUTE_NAME = "routeName";
    private static final String ROUTES_SHORT_DESCRIPTION = "shortDescription";
    private static final String ROUTES_DESCRIPTION = "description";
    private static final String ROUTES_START_POINT_ID = "startPointId";
    private static final String ROUTES_END_POINT_ID = "endPointId";
    private static final String ROUTES_ROUTE_HAS_POINTS_ID = "routePointId";
    private static final String ROUTES_KML_PATH = "kmlPath";

    // points table - column names
    private static final String POINTS_LATITUDE = "latitude";
    private static final String POINTS_LONGITUDE = "longitude";
    private static final String POINTS_ALTITUDE = "altitude";
    private static final String POINTS_SHORTDESC = "shortDesc";
    private static final String POINTS_DESCRIPTION_POINTS = "description";
    private static final String POINTS_DATE = "date";
    private static final String POINTS_ROUTE_ID = "routeId";

    // routeHasPoints table - column names
    private static final String ROUTE_HAS_POINTS_POINT_ID = "pointId";
    private static final String ROUTE_HAS_POINTS_ROUTE_ID = "routeId";

    private static final String NOTICE_TITEL = "title";
    private static final String NOTICE_POINT_ID = "point_id";
    private static final String NOTICE_TEXT = "text";
    private static final String NOTICE_ROUTE_ID = "route_id";
    private static final String NOTICE_PHOTO_PATH = "photo_path";

    public DatabaseHelper(Context context) {
        // save db in device
        // super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // try to save db on card
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
//        db.execSQL(CREATE_TABLE_POINTS);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
//        db.execSQL(CREATE_TABLE_ROUTE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        db.execSQL(CREATE_TABLE_NOTICE);

        // Log.d(LOG, "******db path: " + context.getDatabasePath(DATABASE_NAME));

        //db.execSQL("delete from "+ TABLE_POINTS);
    }

    // Table Create Statements
    // Users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + USERS_NAME + " VARCHAR,"
                + USERS_PASSWORD + " VARCHAR,"
                + USERS_EMAIL + " VARCHAR"
            + ")";

    // Routes table create statement
    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_ROUTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + ROUTES_ROUTE_NAME + " VARCHAR(200), "
                + ROUTES_START_POINT_ID + " INTEGER, "
                + ROUTES_END_POINT_ID + " INTEGER, "
                + ROUTES_ROUTE_HAS_POINTS_ID + " INTEGER, "
                + ROUTES_SHORT_DESCRIPTION + " TEXT, "
                + ROUTES_DESCRIPTION + " TEXT, "
            + ")";

    // Routes table create statement
    private static final String CREATE_TABLE_POINTS = "CREATE TABLE "
            + TABLE_POINTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + POINTS_ROUTE_ID + " INTEGER NOT NULL,"
                + POINTS_LATITUDE + " INTEGER,"
                + POINTS_LONGITUDE + " INTEGER,"
                + POINTS_ALTITUDE + " INTEGER,"
                + POINTS_DESCRIPTION_POINTS + " TEXT,"
                + POINTS_DATE + " TEXT"
            + ")";

    // RouteHasPoints table create statement
    private static final String CREATE_TABLE_ROUTE_HAS_POINTS = "CREATE TABLE "
            + TABLE_ROUTE_HAS_POINTS + "("
                + ROUTE_HAS_POINTS_ROUTE_ID + " INTEGER,"
                + ROUTE_HAS_POINTS_POINT_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_NOTICE = "CREATE TABLE "
            + TABLE_NOTICE + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + NOTICE_POINT_ID + " INTEGER, "
            + NOTICE_ROUTE_ID + " INTEGER, "
            + NOTICE_TEXT + " VARCHAR(2000), "
            + NOTICE_TITEL + " VARCHAR(200),"
            + NOTICE_PHOTO_PATH + " VARCHAR(2000)"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        // db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ROUTE);
        db.execSQL(CREATE_TABLE_POINTS);
        db.execSQL(CREATE_TABLE_NOTICE);
        //db.execSQL(CREATE_TABLE_ROUTE_HAS_POINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICE);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_HAS_POINTS);
        onCreate(db);
    }

//    /**
//     * Creating an user
//     */
//    public long insertUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(USERS_NAME, user.getName());
//        values.put(USERS_PASSWORD, user.getPassword());
//        values.put(USERS_EMAIL, user.getEmail());
//        // values.put(USERS_STAISTIK_ID, user.getStatistikId());
//        // values.put(USERS_ROUTE_FAV_ID, user.getRouteFavId());
//        // values.put(USERS_ROUTE_SELF_ID, user.getRouteSelfId());
//        // insert row
//        long user_id = db.insert(TABLE_USERS, null, values);
//        Log.d("db helper", user_id + ": User created in DB");
//        db.close();
//        return user_id;
//    }
//
//    /**
//     * getting all users
//     */
//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<User>();
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//
//        Log.e(LOG, selectQuery);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                User u = new User();
//                u.setName(c.getString((c.getColumnIndex(USERS_NAME))));
//                u.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
//                u.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//                //u.setRouteFavId(c.getInt(c.getColumnIndex(USERS_ROUTE_FAV_ID)));
//                //u.setRouteSelfId(c.getInt(c.getColumnIndex(USERS_ROUTE_SELF_ID)));
//                //u.setStatistikId(c.getInt(c.getColumnIndex(USERS_STAISTIK_ID)));
//                // adding to tags list
//                users.add(u);
//                Log.d("db helper", u.toString());
//            } while (c.moveToNext());
//        }
//        return users;
//    }
//
//    /**
//     * getting one user
//     */
//    public User getUser(long user_id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
//                + KEY_ID + " = " + user_id;
//        Log.e(LOG, selectQuery);
//        Cursor c = db.rawQuery(selectQuery, null);
//        if (c != null)
//            c.moveToFirst();
//        User u = new User();
//        u.setName(c.getString((c.getColumnIndex(USERS_NAME))));
//        u.setPassword(c.getString(c.getColumnIndex(USERS_PASSWORD)));
//        u.setEmail(c.getString(c.getColumnIndex(USERS_EMAIL)));
//        return u;
//    }
//
//    /**
//     * Deleting an user
//     */
//    public void deleteUser(long user_id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(user_id)});
//    }

    /**
     *    Closing database conection
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * Liefert die Namen der Tabellen.
     * @return ArrayList bestehend aus den Namen aller Tabellen.
     */
    public ArrayList getAllTableName() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        return arrTblNames;
        //for(String s : arrTblNames){
        //    Log.d("********db names", s);
        //}
    }

    /**
    * Fuegt einen Punkt in die Tabelle ein.
    */
    public long insertPoint(Point point) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POINTS_LATITUDE, point.getLatitude());
        values.put(POINTS_LONGITUDE, point.getLongitude());
        values.put(POINTS_ALTITUDE, point.getAltitude());
        values.put(POINTS_DATE, point.getTime());
        values.put(POINTS_ROUTE_ID, point.getRouteId());
        // insert row
        long point_id = db.insert(TABLE_POINTS, null, values);
        Log.d("db helper", point_id + ": Point created in DB");
        return point_id;
    }

    /**
     * Updatet den spezifizierten Punkt.
     */
    public void updatePoint(long id, Point point) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(POINTS_LATITUDE, point.getLatitude());
        values.put(POINTS_LONGITUDE, point.getLongitude());
        values.put(POINTS_ALTITUDE, point.getAltitude());
        values.put(POINTS_DATE, point.getTime());
        values.put(POINTS_ROUTE_ID, point.getRouteId());

        db.update(TABLE_POINTS, values, KEY_ID + "=" + id, null);

    }

    /**
     * Liefert eine ArrayList aller eingetragener Punkte.
     */
    public ArrayList<Point> getAllPoints() {
        ArrayList<Point> points = new ArrayList<Point>();
        String selectQuery = "SELECT * FROM " + TABLE_POINTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Point p = new Point();
                p.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                p.setLatitude(c.getFloat((c.getColumnIndex(POINTS_LATITUDE))));
                p.setLongitude(c.getFloat((c.getColumnIndex(POINTS_LONGITUDE))));
                p.setAltitude(c.getFloat((c.getColumnIndex(POINTS_ALTITUDE))));
                p.setTime(c.getString((c.getColumnIndex(POINTS_DATE))));
                points.add(p);
                Log.d("db helper", p.toString());
            } while (c.moveToNext());
        }
        return points;
    }

    /**
     * Liefert den Punkt, welcher die uebergebene ID hat.
     */
    public Point getPointById(long id) {
        Point p = new Point();
        String selectQuery = "SELECT * FROM " + TABLE_POINTS + " WHERE " + KEY_ID + " =" + id;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            p.setId(c.getInt((c.getColumnIndex(KEY_ID))));
            p.setLatitude(c.getFloat((c.getColumnIndex(POINTS_LATITUDE))));
            p.setLongitude(c.getFloat((c.getColumnIndex(POINTS_LONGITUDE))));
            p.setAltitude(c.getFloat((c.getColumnIndex(POINTS_ALTITUDE))));
            p.setTime(c.getString((c.getColumnIndex(POINTS_DATE))));
        } else {
            p = null;
        }
        return p;
    }

    /**
     * Liefert saemtliche Punkte, die mit der uebergebenen RouteID assoziiert sind als ArrayList.
     */
    public ArrayList<Point> getPointsByRouteId(long id) {
        ArrayList points = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_POINTS + " WHERE " + POINTS_ROUTE_ID + " =" + id;
        //Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Point p = new Point();
                p.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                p.setLatitude(c.getFloat((c.getColumnIndex(POINTS_LATITUDE))));
                p.setLongitude(c.getFloat((c.getColumnIndex(POINTS_LONGITUDE))));
                p.setAltitude(c.getFloat((c.getColumnIndex(POINTS_ALTITUDE))));
                p.setTime(c.getString((c.getColumnIndex(POINTS_DATE))));
                points.add(p);
            } while (c.moveToNext());
        }
        return points;
    }

    /**
     * Fuegt einen Eintrag in die Tabelle der Beziehung zwischen Point und Route ein.
     * @param pointId ID des Points
     * @param routeId ID der Route
     * @return ID des Eintrags.
     */
    public long insertRouteHasPoint(long routeId, long pointId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTE_HAS_POINTS_ROUTE_ID, routeId);
        values.put(ROUTE_HAS_POINTS_POINT_ID, pointId);
        // insert row
        long id = db.insert(TABLE_ROUTE_HAS_POINTS, null, values);
        Log.d("db helper", "insert into " + TABLE_ROUTE_HAS_POINTS + "| id: " + id + "route ID: " + routeId + "point ID: " + pointId);
        return id;
    }

    /**
     * Fuegt eine Route in die Tabelle ein.
     * @param r Einzufuegende Route.
     * @return ID der Route
     */
    public long insertRoute(Route r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTES_ROUTE_NAME, r.getName());
        values.put(ROUTES_START_POINT_ID, r.getStartPoint().getId());
        values.put(ROUTES_END_POINT_ID, r.getEndPoint().getId());
        values.put(ROUTES_ROUTE_HAS_POINTS_ID, r.getId());
        // insert row
        long id = db.insert(TABLE_ROUTES, null, values);
        Log.d("db helper", "insert into " + TABLE_ROUTES + "| id: " + id + "name: " + r.getName() + "startPointId: " + r.getStartPoint().getId() + "endPointId: " + r.getEndPoint().getId() + "routeHasPointId: " + r.getId());
        return id;
    }

    /**
     * Updatet einen Eintrag in der Routentabelle anhand der uebergebenen RouteID.
     */
    public void updateRoute(long id, Route r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTES_ROUTE_NAME, r.getName());
        values.put(ROUTES_START_POINT_ID, r.getStartPoint().getId());
        values.put(ROUTES_END_POINT_ID, r.getEndPoint().getId());
        values.put(ROUTES_ROUTE_HAS_POINTS_ID, r.getId());
        values.put(ROUTES_DESCRIPTION, r.getDescription());
        db.update(TABLE_ROUTES, values, KEY_ID + "=" + id, null);
        Log.d("db helper", "update " + TABLE_ROUTES + "| id: " + id + "name: " + r.getName() + "startPointId: " + r.getStartPoint().getId() + "endPointId: " + r.getEndPoint().getId() + "routeHasPointId: " + r.getId());
    }

    /**
     * Liefert alle in der Datenbank eingetragenen Routen als ArrayList.
     */
    public ArrayList<Route> getAllRoutes() {
        ArrayList<Route> routes = new ArrayList<Route>();
        String selectQuery = "SELECT * FROM " + TABLE_ROUTES;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(c.getInt((c.getColumnIndex(KEY_ID))));

                route.setStartPoint(getPointById(c.getInt((c.getColumnIndex(ROUTES_START_POINT_ID)))));
                route.setEndPoint(getPointById(c.getInt((c.getColumnIndex(ROUTES_END_POINT_ID)))));

                ArrayList<Point> points = getPointsByRouteId(route.getId());
                if(points.size() > 2){
                    // delete start and end points
                    points.remove(0);
                    points.remove(points.size() - 1);
                    for(Point p : points){
                        route.addOtherPoint(p);
                    }
                }
                ArrayList<Notice> notices = getNoticesByRouteId(route.getId());
                for(Notice n : notices){
                    route.addNotice(n);
                }

                route.setDescription(c.getString((c.getColumnIndex(ROUTES_DESCRIPTION))));
                route.setName(c.getString((c.getColumnIndex(ROUTES_ROUTE_NAME))));

                routes.add(route);
               Log.d("db helper", route.toString());
               Log.d("db helper", "****************");
            } while (c.moveToNext());
        }
        return routes;
    }

    /**
     * Fuegt eine Notiz in die Tabelle ein.
     * @param n Einzufuegende Notiz
     * @return ID der eingefuegten Notiz
     */
    public long insertNotice(Picture n){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTICE_POINT_ID, n.getPlace().getId());
        values.put(NOTICE_ROUTE_ID, n.getRouteId());
        values.put(NOTICE_TEXT, n.getText());
        values.put(NOTICE_TITEL, n.getTitle());
        values.put(NOTICE_PHOTO_PATH, n.getImagePath());
        // insert row
        long id = db.insert(TABLE_NOTICE, null, values);
        db.close();
        return id;
    }

    /**
     * Erzeugt eine ArrayList aus Notizen, welche mit der uebergebenen RouteID assoziiert sind.
     * @param routeId ID der Route
     * @return Liste an Notizen der Route
     */
    public ArrayList<Notice> getNoticesByRouteId(long routeId){
        ArrayList<Notice> notices = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_NOTICE + " WHERE " + NOTICE_ROUTE_ID + " =" + routeId;
        //Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Picture p = new Picture();
                p.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                p.setPlace(getPointById(c.getColumnIndex(NOTICE_POINT_ID)));
                p.setTitle(c.getString((c.getColumnIndex(NOTICE_TITEL))));
                p.setText(c.getString((c.getColumnIndex(NOTICE_TEXT))));
                p.setImagePath(c.getString(c.getColumnIndex(NOTICE_PHOTO_PATH)));
                notices.add(p);
            } while (c.moveToNext());
        }
        return notices;
    }

}