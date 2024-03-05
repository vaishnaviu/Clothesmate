package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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

        ImageView imageView = convertView.findViewById(R.id.imageView);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView dateView = convertView.findViewById(R.id.txtDate);
        TextView idView = convertView.findViewById(R.id.txtId);
        TextView typeView = convertView.findViewById(R.id.txtType);

        checkBox.setChecked(getItem(position).isSelected());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(position).setSelected(checkBox.isChecked());
            }
        });

        dateView.setText(getItem(position).getDateTime());
        idView.setText(getItem(position).getId());
        typeView.setText(getItem(position).getType());

        if (getItem(position).getType().equals("pants")) {
            imageView.setImageResource(R.drawable.image_pants);
        } else if (getItem(position).getType().equals("dress")) {
            imageView.setImageResource(R.drawable.image_dress);
        } else if (getItem(position).getType().equals("shirt")) {
            imageView.setImageResource(R.drawable.image_shirt);
        } else if (getItem(position).getType().equals("shoes")) {
            imageView.setImageResource(R.drawable.image_shoes);
        } else {
            imageView.setImageResource(R.drawable.image_no_style);
        }

        return convertView;
    }
}