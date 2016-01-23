package com.proto.buddy.mountainbuddyv2.activities.Main_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.activities.MainActivity;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback, LocationListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FragmentManager fragmentManager;

    private final static int MILLISECONDS_BETWEEN_UPDATES = 2000;
    private final static int METERS_BETWEEN_UPDATES = 10;

    private TextView distanceText;
    private TextView heightText;
    private Button startButton;
    private Button stopButton;
    private ImageButton pictureButton;
    private ImageButton noticeButton;

    /**
     * Instance of LocationManager class, used to help with GPS data processing
     */
    private LocationManager locationManager;
    /**
     * The route that is currently being tracked.
     */
    private Route route;
    /**
     * Latitude of the current position.
     */
    private double latitude;
    /**
     * Longitude of the current position.
     */
    private double longitude;
    /**
     * Altitude of the current position.
     */
    private double altitude;
    /**
     * Current time.
     */
    private String time;
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
    private GoogleMap map;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        distanceText = (TextView) this.getActivity().findViewById(R.id.main_infor_bar_distance);
        heightText = (TextView) this.getActivity().findViewById(R.id.main_infor_bar_max_height);
        startButton = (Button) this.getActivity().findViewById(R.id.button_start);
        /*pictureButton = (ImageButton) this.getActivity().findViewById(R.id.button_takePic);
        noticeButton = (ImageButton) this.getActivity().findViewById(R.id.button_notice);*/
        /*pictureButton.setClickable(false);
        noticeButton.setClickable(false);*/
        /*startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoute();
            }
        });*/
       /* pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: insert method for taking and saving a picture
            }
        });
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create method for opening a dialogue in which the user can input text to create notice
            }
        });*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void startRoute(){
        this.route = new Route();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MILLISECONDS_BETWEEN_UPDATES, METERS_BETWEEN_UPDATES, this);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MILLISECONDS_BETWEEN_UPDATES, METERS_BETWEEN_UPDATES, this);
        pictureButton.setClickable(true);
        noticeButton.setClickable(true);
    }

    private void stopRoute(){
        //TODO: insert steps to save route, opening a dialogue (options: cancel, save route, stop route and do not save) etc
    }

    @Override
    public void onLocationChanged(Location location) {

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

    private boolean isBetterLocation(Location previousLocation, Location newLocation){
        final int ONE_MINUTE = 1000 * 60;
        if(previousLocation == null){
            return true;
        }

        long deltaTime = newLocation.getTime() - previousLocation.getTime();
        boolean newerThanOneMin = deltaTime > ONE_MINUTE;
        boolean olderThanOneMin = deltaTime < -ONE_MINUTE;
        boolean newer = deltaTime > 0;

        if(newerThanOneMin){
            return true;
        }else if(olderThanOneMin){
            return false;
        }

        float deltaAccuracy = newLocation.getAccuracy() - previousLocation.getAccuracy();
        boolean isLessAccurate = deltaAccuracy > 0;
        boolean isMoreAccurate = deltaAccuracy < 0;
        if(isMoreAccurate){
            return true;
        }else if(newer && !isLessAccurate){
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
