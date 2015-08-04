package com.proto.buddy.mountainbuddyv2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.database.DatabaseHelper;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;


public class ListActivity extends android.app.ListActivity {

    private DatabaseHelper db;
    private ArrayList<Route> routes;
    BaseAdapter myAdapter;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choose_route);

        db = new DatabaseHelper(getApplicationContext());
        db.getWritableDatabase();

        db.getAllRoutes();
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        routes = dbHelper.getAllRoutes();

        // initiate the listadapter
        myAdapter = new RouteListAdapter<String>(this, routes);
        // assign the list adapter
        setListAdapter(myAdapter);


    }

    // when an item of the list is clicked

    @Override

    protected void onListItemClick(ListView list, View view, int position, long id) {

        super.onListItemClick(list, view, position, id);


        Route selected = (Route) myAdapter.getItem(position);

        Intent i = new Intent(ListActivity.this, mainActivity.class);
        i.putExtra("Route", selected);
        startActivity(i);

        Toast.makeText(this.getApplicationContext(), "Test" + position, Toast.LENGTH_SHORT).show();

        //String selectedItem = (String) getListAdapter().getItem(position);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
