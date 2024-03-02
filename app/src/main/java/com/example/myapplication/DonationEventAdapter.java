package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DonationEventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int mResource;

    public DonationEventAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView dateView = convertView.findViewById(R.id.txtDate);
        TextView idView = convertView.findViewById(R.id.txtId);
        TextView typeView = convertView.findViewById(R.id.txtType);

        dateView.setText(getItem(position).getDateTime());
        idView.setText(getItem(position).getId());
        typeView.setText(getItem(position).getType());

        return convertView;
    }
}