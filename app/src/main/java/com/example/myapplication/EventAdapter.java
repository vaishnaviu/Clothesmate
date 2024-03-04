package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;

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
        TextView idView = convertView.findViewById(R.id.txtId);
        //imageView.setImageResource(getItem(position).getImage());

        //need to add an if statement to change the text
        dateView.setText(getItem(position).getDateTime());
        idView.setText(getItem(position).getId());
        if (getItem(position).getNewUse() == 1) {
            //dateView.setTypeface(null, Typeface.BOLD);
            //idView.setTypeface(null, Typeface.BOLD);
            System.out.println("changing text to RED");
            dateView.setTextColor(Color.RED);
            idView.setTextColor(Color.RED);
        }else{
            //dateView.setTypeface(null, Typeface.NORMAL);
            //idView.setTypeface(null, Typeface.NORMAL);
            System.out.println("changing text back to BLACK");
            dateView.setTextColor(Color.BLACK);
            idView.setTextColor(Color.BLACK);
        }
        imageViewDot.setImageResource(R.drawable.donation_selector);
        //imageViewEventDelete.setImageResource(R.drawable.ic_styling_press);

        /*imageViewEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("ourtest").child(getItem(position).getDateTime()).child("status").setValue(0);
            }
        });*/
        if(getItem(position).getType()!=null){
            if(getItem(position).getType().equals("pants")){
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }else if(getItem(position).getType().equals("dress")){
                imageView.setImageResource(R.drawable.ic_mycloset_press);
            }
        }


        return convertView;

    }

}
