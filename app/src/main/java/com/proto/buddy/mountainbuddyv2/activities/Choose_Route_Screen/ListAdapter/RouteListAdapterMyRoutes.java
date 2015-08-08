package com.proto.buddy.mountainbuddyv2.activities.Choose_Route_Screen.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Phillip on 29.07.2015.
 */
public class RouteListAdapterMyRoutes<U> extends BaseAdapter {
    /*
    to inflate layout
     */
    private final LayoutInflater mInflater;
    /*
    data list
     */
    private ArrayList<Route> data;
    /*
    alternative data set
     */
    private Map<String, Route> mapData;

    Route item;

    public RouteListAdapterMyRoutes(Context context, ArrayList<Route> data) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Route getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * View holder pattern to instantiate the view elements only once
     */
    static class ViewHolder {
        TextView title;
    }

    /**
     * Inflates view for each listItem in the data list
     * @param position the position of the item in the list view
     * @param convertView view of the item
     * @param parent complete list
     * @return view for each list row
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_layout, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.list_item_title);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.get(position).getName());

        return convertView;
    }

    /**
     * Sets new data list and refreshes view
     * @param pData new data list
     */
    public void changeData(ArrayList<Route> pData){
        data = pData;
        this.notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

}
