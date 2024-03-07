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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DonationEventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int mResource;
    private CheckBoxListener checkBoxListener;

    public interface CheckBoxListener {
        void onCheckBoxChanged(Event event, boolean isChecked);
    }

    public void setCheckBoxListener(CheckBoxListener listener) {
        this.checkBoxListener = listener;
    }

    private void onCheckBoxStateChanged(Event event, boolean isChecked) {
        if (checkBoxListener != null) {
            checkBoxListener.onCheckBoxChanged(event, isChecked);
        }
    }

    public DonationEventAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imageView2);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        TextView dateView = convertView.findViewById(R.id.txtDate);
        TextView idView = convertView.findViewById(R.id.txtId);
        TextView typeView = convertView.findViewById(R.id.txtType);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(getItem(position).getDateTime(), formatter);
        String formattedDate = dateTime.format(formatter);

        dateView.setText(formattedDate);
        typeView.setText(getItem(position).getType());
        idView.setText(getItem(position).getId());

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

        Event currentEvent = getItem(position);
        checkBox.setChecked(getItem(position).getStatus() == 1);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentEvent.setStatus(isChecked ? 1 : 0);
            onCheckBoxStateChanged(currentEvent, isChecked);
        });

        return convertView;
    }
}