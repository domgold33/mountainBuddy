package com.proto.buddy.mountainbuddyv2.activities.Main_screen;

import android.app.Activity;
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
import android.widget.Button;
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
import com.proto.buddy.mountainbuddyv2.activities.RouteSave;
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

    private final static int MILLISECONDS_BETWEEN_UPDATES = 2000;
    private final static int METERS_BETWEEN_UPDATES = 10;

    private TextView distanceText;
    private TextView heightText;
    private Button startButton;
    private Button stopButton;
    private Button pictureButton;
    private ImageButton noticeButton;

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
        fragmentManager = this.getActivity().getFragmentManager();
        if (getArguments() != null) {
        }
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context = getActivity().getApplicationContext();
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

                // start new fragment

                /*// save to db
                // save to db
                RemoteDatabaseHelper db = new RemoteDatabaseHelper(context);
                Route r = routeRecorder.getRoute();

                JSONObject route = new JSONObject();
                try {
                    route.put("routeName", "test");
                    route.put("description", "desc");
                    route.put("startPointId", 1);
                    route.put("endPointId", 2);
                    route.put("routePointId", 2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, route.toString());*/

                //db.POST(db.URL_REMOTE_SERVER + "/routes", );

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
        //TODO: insert steps to save route, opening a dialogue (options: cancel, save route, stop route and do not save) etc
        routeRecorder.cancelLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map Received");
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null)
        {
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

}
