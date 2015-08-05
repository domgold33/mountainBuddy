package com.proto.buddy.mountainbuddyv2.activities;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.proto.buddy.mountainbuddyv2.R;
import com.proto.buddy.mountainbuddyv2.model.Route;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Phillip on 05.08.2015.
 */
public class NavigationListAdapter extends BaseAdapter{

    private ArrayList<String> menuPoints;

    private ArrayList<Image> menuIcons;

    private Context context;

    private LayoutInflater mInflater;

    public NavigationListAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.menuPoints = new ArrayList<String>();
        this.menuIcons = new ArrayList<Image>();
        setMenuItems();
    }

    public void setMenuItems(){
        menuPoints.add(context.getString(R.string.title_section1));

        //menuIcons.add();

        menuPoints.add(context.getString(R.string.title_section2));

        //menuIcons.add();

        menuPoints.add(context.getString(R.string.title_section3));

        //menuIcons.add();
    }


    @Override
    public int getCount() {
        return menuPoints.size();
    }

    @Override
    public Object getItem(int position) {
        return menuPoints.get(position);
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
        ImageButton icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.drawer_row_layout, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.drawer_item_title);
            holder.icon = (ImageButton) convertView.findViewById(R.id.drawer_item_icon);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(menuPoints.get(position));

        //holder.icon.setImageResource();

        return convertView;
    }
}
