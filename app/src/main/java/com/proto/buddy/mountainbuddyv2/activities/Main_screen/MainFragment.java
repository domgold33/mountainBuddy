package com.proto.buddy.mountainbuddyv2.activities.Main_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.AppLogic.RouteRecorder;
import com.proto.buddy.mountainbuddyv2.activities.MainActivity;
import com.proto.buddy.mountainbuddyv2.model.Route;


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
    private RouteRecorder routeRecorder;
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
        }
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        distanceText = (TextView) this.getActivity().findViewById(R.id.main_infor_bar_distance);
        heightText = (TextView) this.getActivity().findViewById(R.id.main_infor_bar_max_height);
        startButton = (Button) this.getActivity().findViewById(R.id.button_start);
        pictureButton = (ImageButton) this.getActivity().findViewById(R.id.button_takePic);
        noticeButton = (ImageButton) this.getActivity().findViewById(R.id.button_notice);
        pictureButton.setClickable(false);
        noticeButton.setClickable(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoute();
            }
        });
        pictureButton.setOnClickListener(new View.OnClickListener() {
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
        });
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

    /**
     * Upon button press, activates the processing of location data, starting the route tracking process.
     */
    private void startRoute(){
        if(map != null){
            this.route = new Route();
            routeRecorder = new RouteRecorder(route, map, distanceText, heightText, locationManager);
            routeRecorder.requestLocationUpdates(LocationManager.GPS_PROVIDER);
//        routeRecorder.requestLocationUpdates(LocationManager.NETWORK_PROVIDER);
            pictureButton.setClickable(true);
            noticeButton.setClickable(true);
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
