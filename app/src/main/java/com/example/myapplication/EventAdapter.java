package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private int mResource;
    public EventAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;

    }



    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        ImageView imageViewDot = convertView.findViewById(R.id.imageViewDot);
        //ImageView imageViewEventDelete = convertView.findViewById(R.id.imageViewEventDelete);
        TextView dateView = convertView.findViewById(R.id.txtDate);
        TextView timeView = convertView.findViewById(R.id.txtTime);
        TextView typeView = convertView.findViewById(R.id.txtType);
        TextView idView = convertView.findViewById(R.id.txtId);
        //imageView.setImageResource(getItem(position).getImage());

        //need to add an if statement to change the text
        dateView.setText(getItem(position).getDate());
        timeView.setText(getItem(position).getTime());
        typeView.setText(getItem(position).getType());
        idView.setText("ID: " +getItem(position).getId());
        if (getItem(position).getNewUse() == 1) {
            imageViewDot.setImageResource(R.drawable.ic_new);
        }

        //imageViewEventDelete.setImageResource(R.drawable.ic_styling_press);

        /*imageViewEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ourtest").child(getItem(position).getDateTime()).child("status").setValue(0);
            }
        });*/
        if(getItem(position).getType()!=null){
            if(getItem(position).getType().equals("pants")){
                imageView.setImageResource(R.drawable.image_pants);
            }else if(getItem(position).getType().equals("dress")){
                imageView.setImageResource(R.drawable.image_dress);
            }else if(getItem(position).getType().equals("shirt")){
                imageView.setImageResource(R.drawable.image_shirt);
            }else if(getItem(position).getType().equals("shoes")){
                imageView.setImageResource(R.drawable.image_shoes);
            }else if(getItem(position).getType().equals("jacket")) {
                imageView.setImageResource(R.drawable.image_jacket);
            }
        }else{
            imageView.setImageResource(R.drawable.image_no_style);
        }


        return convertView;

    }

}
