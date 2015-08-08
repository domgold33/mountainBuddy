package com.proto.buddy.mountainbuddyv2.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.database.DatabaseHelper;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RouteFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseHelper db;

    private ArrayList<Route> routes;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView_all;

    private AbsListView mListView_my;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;


    private FragmentManager fragmentManager;

    // TODO: Rename and change types of parameters
    public static RouteFragment newInstance() {
        RouteFragment fragment = new RouteFragment();
        Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = this.getActivity().getSupportFragmentManager();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        db.getWritableDatabase();

        db.getAllRoutes();
        DatabaseHelper dbHelper = new DatabaseHelper(this.getActivity().getApplicationContext());
        routes = dbHelper.getAllRoutes();

        // initiate the listadapter
        mAdapter = new RouteListAdapter<String>(this.getActivity().getApplicationContext(), routes);
        // assign the list adapter



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_route, container, false);

        // Set the adapter
        mListView_all = (AbsListView) view.findViewById(R.id.list_all_routes);
        ((AdapterView<ListAdapter>) mListView_all).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView_all.setOnItemClickListener(this);


        // Set the adapter
        mListView_my = (AbsListView) view.findViewById(R.id.list_my_routes);
        ((AdapterView<ListAdapter>) mListView_my).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView_my.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((mainActivity) activity).onSectionAttached(3);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this.getActivity().getApplicationContext(),"Should work", Toast.LENGTH_LONG).show();
        fragmentManager.beginTransaction()
                .replace(R.id.container, RouteItemFragment.newInstance()).addToBackStack("List_to_Item")
                .commit();
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
        public void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TabHost tabhost = (TabHost) getActivity().findViewById(R.id.myTabHost);
        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Alle Routen");
        tabhost.addTab(ts);
        ts = tabhost.newTabSpec("tag2");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Meine Routen");
        tabhost.addTab(ts);

    }
}
