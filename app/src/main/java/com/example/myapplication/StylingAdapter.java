package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class StylingAdapter extends ArrayAdapter<Map<String, Object>> {
    private Context mContext;
    private int mResource;

    public StylingAdapter(@NonNull Context context, int resource, @NonNull List<Map<String, Object>> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }

        Map<String, Object> item = getItem(position);

        if (item != null) {
            TextView idTextView = convertView.findViewById(R.id.txtId);
            TextView typeTextView = convertView.findViewById(R.id.txtType);
            TextView colorTextView = convertView.findViewById(R.id.txtColor);
            ImageView imageView = convertView.findViewById(R.id.imageView2);

            String id = item.get("Id") != null ? item.get("Id").toString() : "";
            String type =item.get("Type") != null ? item.get("Type").toString() : "";
            String color = item.get("Color") != null ? item.get("Color").toString() : "";

            if (getItem(position).get("Type").equals("pants")) {
                imageView.setImageResource(R.drawable.image_pants);
            } else if (getItem(position).get("Type").equals("dress")) {
                imageView.setImageResource(R.drawable.image_dress);
            } else if (getItem(position).get("Type").equals("shirt")) {
                imageView.setImageResource(R.drawable.image_shirt);
            } else if (getItem(position).get("Type").equals("shoes")) {
                imageView.setImageResource(R.drawable.image_shoes);
            } else if (getItem(position).get("Type").equals("jacket")) {
                imageView.setImageResource(R.drawable.image_jacket);
            } else {
                imageView.setImageResource(R.drawable.image_no_style);
            }

            idTextView.setText("ID: " + id);
            typeTextView.setText("Type: " +type);
            colorTextView.setText(color);
        }

        return convertView;
    }
}