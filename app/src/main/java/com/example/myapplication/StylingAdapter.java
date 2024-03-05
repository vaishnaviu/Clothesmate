package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class StylingAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int mResource;

    public StylingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView typeView = convertView.findViewById(R.id.txtType);
        TextView colorView = convertView.findViewById(R.id.txtColor);

        Event currentEvent = getItem(position);

        if (currentEvent != null) {
            typeView.setText(currentEvent.getType());
            colorView.setText(currentEvent.getColor());
        }

        return convertView;
    }
}