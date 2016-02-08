package com.proto.buddy.mountainbuddyv2.activities.Main_screen;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.AppLogic.RouteRecorder;
import com.proto.buddy.mountainbuddyv2.activities.MainActivity;
import com.proto.buddy.mountainbuddyv2.database.DatabaseHelper;
import com.proto.buddy.mountainbuddyv2.database.RemoteDatabaseHelper;
import com.proto.buddy.mountainbuddyv2.model.Notice;
import com.proto.buddy.mountainbuddyv2.model.Picture;
import com.proto.buddy.mountainbuddyv2.model.Point;
import com.proto.buddy.mountainbuddyv2.model.Route;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String TAG = "MainFragment";

    private OnFragmentInteractionListener mListener;

    private FragmentManager fragmentManager;

    private TextView distanceText;
    private TextView heightText;
    private Button startButton;
    private Button stopButton;
    private Button pictureButton;
    private ImageButton noticeButton;
    private DatabaseHelper db;

    /**
     * Instance of LocationManager class, used to help with GPS data processing
     */
    private LocationManager locationManager;
    /**
     * The route that is currently being tracked.
     */
    private Route route;
    private RouteRecorder routeRecorder;
    private GoogleMap map;

    private Context context;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        distanceText = (TextView) rootView.findViewById(R.id.main_infor_bar_distance);
        heightText = (TextView) rootView.findViewById(R.id.main_infor_bar_max_height);
        startButton = (Button) rootView.findViewById(R.id.button_start);
        stopButton = (Button) rootView.findViewById(R.id.button_stop);
        pictureButton = (Button) rootView.findViewById(R.id.button_takePic);
        noticeButton = (ImageButton) rootView.findViewById(R.id.button_notice);
        pictureButton.setClickable(false);
        noticeButton.setClickable(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                startRoute();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeRecorder.cancelLocationUpdates();
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
                stopRoute();
            }
        });
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File imgFile = createImageFile();
                    takePictureAndSave(imgFile);
                }catch (IOException e){
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create method for opening a dialogue in which the user can input text to create notice
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getActivity().getApplicationContext();
        fragmentManager = this.getActivity().getFragmentManager();
        if (getArguments() != null) {
        }
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        // init Database
        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        db.getWritableDatabase();
        ArrayList<String> arr = db.getAllTableName();
        Log.d(TAG, "*** Tables: ");
        for (String s : arr){
            Log.d(TAG, "****** " + s);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
    
    private void takePictureAndSave(File file){
        Notice picture = new Picture(route.getOtherPoints().get(route.getOtherPoints().size() - 1), "new route", "new route", file.getPath());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            if (file != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                startActivityForResult(takePictureIntent, 1);
            }
        }
        this.route.addNotice(picture);
        Log.d(TAG, String.valueOf(route.getListOfNotices().size()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            ((MainActivity) activity).onSectionAttached(1);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment mapFragment = (MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            getActivity().getFragmentManager().beginTransaction()
                    .remove(mapFragment).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Upon button press, activates the processing of location data, starting the route tracking process.
     */
    private void startRoute(){
        if(map != null){
            Log.d(TAG, "Start Route ------------");
            this.route = new Route();
            routeRecorder = new RouteRecorder(route, map, distanceText, heightText, locationManager);
            //routeRecorder.requestLocationUpdates(LocationManager.GPS_PROVIDER);
            routeRecorder.requestLocationUpdates(LocationManager.NETWORK_PROVIDER);
//            pictureButton.setClickable(true);
//            noticeButton.setClickable(true);
        }else{
            Toast.makeText(this.getActivity().getApplicationContext(), "Please wait for the map to be loaded", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Upon button press, stops processing of location data, ending the route tracking process. The route will then be saved in7
     * the database.
     */
    private void stopRoute(){
        routeRecorder.cancelLocationUpdates();
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(this.route != null){
            dialog.setContentView(R.layout.stop_dialog);
            dialog.setTitle("Moechten Sie die Route speichern?");
            Button btnSave = (Button)dialog.findViewById(R.id.save);
            Button btnCancel = (Button)dialog.findViewById(R.id.cancel);

            btnSave.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Save");
                    EditText routeName = (EditText) dialog.findViewById(R.id.routeName);
                    EditText routeDescription = (EditText) dialog.findViewById(R.id.routeDescription);
                    // set route name
                    route.setName((routeName.getText().toString()));
                    route.setDescription((routeDescription.getText().toString()));
                    saveRoute();
                    dialog.cancel();
                }
            });
            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Stop Route");
                    dialog.cancel();
                }
            });
        }
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map Received");
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 17));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /**
     * Speichert die Route sowie sämtliche aufgezeichneten Punkte in der lokalen Datenbank, nachdem die Aufzeichnung gestoppt und ein Name
     * für die Route eingegeben wurde.
     */
    private void saveRoute() {
        DatabaseHelper db = new DatabaseHelper(this.getActivity().getApplicationContext());
        db.getWritableDatabase();

        Log.d(TAG, "********* Save route *********");

        this.route = routeRecorder.getRoute();

//        this.route.setName("Test route");
//        this.route.setDescription("Hello, im a new route");

        // insert start point
        long startPointID = db.insertPoint(route.getStartPoint());
        this.route.getStartPoint().setId(startPointID);

        Log.d(TAG, "***Start point***");
        Log.d(TAG, "Latitude: " + route.getStartPoint().getLatitude());
        Log.d(TAG, "Longitude: " + route.getStartPoint().getLongitude());
        Log.d(TAG, "Altitude: " + route.getStartPoint().getAltitude());
        Log.d(TAG, "Time: " + route.getStartPoint().getTime());

        ArrayList<Point> points = route.getOtherPoints();

        // get last point from getOtherPoints, make it end point and delete it from OtherPoints
        if(points.size() > 0) {
//            Point endPoint = points.get(points.size() - 1);
            Point endPoint = points.remove(points.size() - 1);

            this.route.setEndPoint(endPoint);

            // set dummy route to get the id
            long routeId = db.insertRoute(route);
            Log.d(TAG, "route id " + routeId);

            // save kml
            /*String kmlPath = getFilesDir().getPath() + route.getName() + "RouteId-" + routeId;
            route.setKmlPath(kmlPath + ".kml");*/
            // update points route id
            route.getStartPoint().setRouteId(routeId);
            db.updatePoint(startPointID, route.getStartPoint());
            Log.d(TAG, "points.size() " + points.size());
            Log.d(TAG, "points " + points.toString());
            if(points.size() > 0){
                for (Iterator<Point> it = points.iterator(); it.hasNext(); ) {
                    Point p = it.next();
                    Log.d(TAG, "point " + p.toString());

                    long pointID = db.insertPoint(p);
                    p.setId(pointID);
                    p.setRouteId(routeId);

                    // update points route id
                    db.updatePoint(pointID, p);

                    Log.d(TAG, "***Other point***");
                    Log.d(TAG, "Latitude: " + p.getLatitude());

                    Log.d(TAG, "Longitude: " + p.getLongitude());
                    Log.d(TAG, "Altitude: " + p.getAltitude());
                    Log.d(TAG, "Time: " + p.getTime());
                }
            }
            ArrayList<Notice> notices = route.getListOfNotices();
            if(notices.size() > 0){
                for(Iterator<Notice> it = notices.iterator(); it.hasNext(); ){
                    Picture p = (Picture) it.next();
                    long pointID = db.insertPoint(p.getPlace());
                    p.getPlace().setId(pointID);
                    p.setRouteId(routeId);
                    long noticeID = db.insertNotice(p);
                    p.setId((int) noticeID);
                }
            }
            Log.d(TAG, "***End point***");
            Log.d(TAG, "Latitude: " + this.route.getEndPoint().getLatitude());
            Log.d(TAG, "Longitude: " + this.route.getEndPoint().getLongitude());
            Log.d(TAG, "Altitude: " + this.route.getEndPoint().getAltitude());
            Log.d(TAG, "Time: " + this.route.getEndPoint().getTime());
            long endPointID = db.insertPoint(this.route.getEndPoint());
            this.route.getEndPoint().setRouteId(routeId);
            this.route.getEndPoint().setId(endPointID);
            // update points route id
            db.updatePoint(endPointID, this.route.getEndPoint());

            // insert notice in db
            Log.d(TAG, "************getListOfNotices: " + route.getListOfNotices().size());

            if(route.getListOfNotices().size() > 0 ){
                for(Notice n : route.getListOfNotices()){
                    Picture p = (Picture) n;
                    p.setRouteId(routeId);
                    db.insertNotice(p);
                }
            } else {
                Log.d(TAG, "There are no pics: ");
            }

            // update route
            db.updateRoute(routeId, this.route);
        } else {
            Log.d(TAG, "There is no other points and end point");
        }
        map.clear();
    }

}
